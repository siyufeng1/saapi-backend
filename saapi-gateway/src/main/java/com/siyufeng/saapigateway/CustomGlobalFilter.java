package com.siyufeng.saapigateway;

import com.siyufeng.saapiclientsdk.utils.SignUtils;
import com.siyufeng.siapicommon.model.entity.InterfaceInfo;
import com.siyufeng.siapicommon.model.entity.User;
import com.siyufeng.siapicommon.service.InnerInterfaceInfoService;
import com.siyufeng.siapicommon.service.InnerUserInterfaceInfoService;
import com.siyufeng.siapicommon.service.InnerUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.*;

import static java.lang.System.currentTimeMillis;

/**
 * 全局过滤
 */

@Slf4j
@Component
public class CustomGlobalFilter implements GlobalFilter, Ordered {

    @DubboReference
    private InnerUserService innerUserService;

    @DubboReference
    private InnerInterfaceInfoService innerInterfaceInfoService;

    @DubboReference
    private InnerUserInterfaceInfoService innerUserInterfaceInfoService;


    private static final String INTERFACE_HOST = "http://localhost:8123";

    //请求白名单
    public static final List<String> IP_WHITE_LIST = Arrays.asList("127.0.0.1");

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        1. 用户发送请求到 API 网关
//        2. 请求日志
        ServerHttpRequest request = exchange.getRequest();
        log.info("请求唯一标识" + request.getId());
        log.info("请求方法：" + request.getMethod());
        log.info("请求路径：" + request.getPath().value());
        log.info("请求参数：" + request.getQueryParams());
        String sourceAddress = request.getLocalAddress().getHostString();
        log.info("请求来源地址" + request.getRemoteAddress());
        log.info("请求来源地址" + sourceAddress);
//        3. （黑白名单）
        ServerHttpResponse response = exchange.getResponse();
        if (!IP_WHITE_LIST.contains(sourceAddress)) {
            handleNoAuth(response);
        }
//        4. 用户鉴权（判断 accessKey, secretKey 是否合法）
        String accessKey = request.getHeaders().getFirst("accessKey");
        String nonce = request.getHeaders().getFirst("nonce");
        String timestamp = request.getHeaders().getFirst("timestamp");
        String sign = request.getHeaders().getFirst("sign");
        String body = request.getHeaders().getFirst("body");
        //实际情况应该去数据库中查是否已经分配给用户
        User user = null;
        try {
            user = innerUserService.getInvokeUser(accessKey);
        }catch (Exception e){
            log.error("用户不存在");
        }
        if (user == null) {
            return handleNoAuth(response);
        }
//        if (!accessKey.equals("syf")) {
//            return handleNoAuth(response);
//        }
        if (new Long(nonce) > 10000L) {
            return handleNoAuth(response);
        }
        Date nowDate = new Date(currentTimeMillis());
        Date sendDate = new Date(new Long(timestamp));
        if (nowDate.getHours() != sendDate.getHours() || nowDate.getMinutes() - sendDate.getMinutes() > 5) {
            return handleNoAuth(response);
        }

        //实际情况中是从数据库中查出secretKey
        String secretKey = user.getSecretKey();
        String serverSign = SignUtils.getSign(body, secretKey);
        if (sign == null || !sign.equals(serverSign)) {
            return handleNoAuth(response);
        }

//        5. 请求的模拟接口是否存在
        //从数据库中查询模拟接口是否存在，以及请求方法是否匹配，校验请求参数
        InterfaceInfo interfaceInfo = null;
        try {
            interfaceInfo = innerInterfaceInfoService.getInterfaceInfo(INTERFACE_HOST + request.getPath().value(), request.getMethod().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (interfaceInfo == null) {
            return handleNoAuth(response);
        }

        //是否还有接口调用次数
        if(innerUserInterfaceInfoService.invokeCountBiggerThanZero(interfaceInfo.getId(), user.getId()) < 0){
            return handleNoAuth(response);
        }

//        6. 请求转发，调用模拟接口
//        Mono<Void> filter = chain.filter(exchange);
//        7. 响应日志
        return handleResponse(exchange, chain,interfaceInfo.getId(), user.getId());
//        8. 调用成功，次数 + 1
//        9. 调用失败，返回一个规范的错误码
//        if (response.getStatusCode() != HttpStatus.OK) {
//            return handleInvokeError(response);
//        }
//        log.info("custom global filter");
//        return filter;
    }

    @Override
    public int getOrder() {
        return -1;
    }

    public Mono<Void> handleNoAuth(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.FORBIDDEN);
        return response.setComplete();
    }

    public Mono<Void> handleInvokeError(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        return response.setComplete();
    }


    public Mono<Void> handleResponse(ServerWebExchange exchange, GatewayFilterChain chain, long interfaceId, long userId) {
        try {
            ServerHttpResponse originalResponse = exchange.getResponse();
            //缓存数据的工厂
            DataBufferFactory bufferFactory = originalResponse.bufferFactory();
            //拿到响应码
            HttpStatus statusCode = originalResponse.getStatusCode();

            if (statusCode == HttpStatus.OK) {
                //装饰，增强能力
                ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
                    //等调用完转发的接口后才会执行
                    @Override
                    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                        log.info("body instanceof Flux: {}", (body instanceof Flux));
                        if (body instanceof Flux) {
                            Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                            //往返回值里写数据
                            //拼接字符串
                            return super.writeWith(
                                    fluxBody.map(dataBuffer -> {
                                        //7、调用成功后，次数+ 1,其实就是修改数据库 invokeCount
                                        if (originalResponse.getStatusCode() == HttpStatus.OK) {
                                            try {
                                                innerUserInterfaceInfoService.invokeCount(interfaceId, userId);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        byte[] content = new byte[dataBuffer.readableByteCount()];
                                        dataBuffer.read(content);
                                        DataBufferUtils.release(dataBuffer);//释放掉内存
                                        // 构建日志
                                        StringBuilder sb2 = new StringBuilder(200);
                                        sb2.append("<--- {} {} \n");
                                        List<Object> rspArgs = new ArrayList<>();
                                        rspArgs.add(originalResponse.getStatusCode());
                                        //rspArgs.add(requestUrl);
                                        String data = new String(content, StandardCharsets.UTF_8);//data
                                        sb2.append(data);
                                        //打印日志
                                        log.info("响应结果" + data);
                                        return bufferFactory.wrap(content);
                                    }));
                        } else {
                            //8.TODO 调用失败后，返回规范的错误码
                            log.error("<--- {} 响应code异常", getStatusCode());
                        }
                        return super.writeWith(body);
                    }
                };
                //设置response对象为装饰过的
                return chain.filter(exchange.mutate().response(decoratedResponse).build());
            }
            return chain.filter(exchange);//降级处理返回数据
        } catch (Exception e) {
            log.error("网关处理响应错误" + e);
            return chain.filter(exchange);
        }
    }
}