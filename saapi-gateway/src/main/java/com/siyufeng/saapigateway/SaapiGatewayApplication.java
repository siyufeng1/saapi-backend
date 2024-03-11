package com.siyufeng.saapigateway;

import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

@SpringBootApplication(exclude = {
        DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class})
@EnableDubbo
@Service
public class SaapiGatewayApplication {


//    @DubboReference
//    private DemoService demoService;

    public static void main(String[] args) {
        ConfigurableApplicationContext ioc = SpringApplication.run(SaapiGatewayApplication.class, args);
//        SaapiGatewayApplication application = ioc.getBean(SaapiGatewayApplication.class);
//        String result1 = application.doSayHello("syf");
//        String result2 = application.doSayHello2("syf");
//        System.out.println("result1 = " + result1);
//        System.out.println("result2 = " + result2);
    }


//    public String doSayHello(String name) {
//        return demoService.sayHello(name);
//    }
//
//    public String doSayHello2(String name) {
//        return demoService.sayHello2(name);
//    }
//    @Bean
//    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
//        return builder.routes()
//                .route("tobaidu", r -> r.path("/baidu")
//                        .uri("https://www.baidu.com"))
//                .route("tobing", r -> r.path("/bing")
//                        .uri("https://www.bing.com"))
//                .build();
//    }
}
