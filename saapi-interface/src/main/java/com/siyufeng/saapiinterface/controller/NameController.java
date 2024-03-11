package com.siyufeng.saapiinterface.controller;


import com.siyufeng.saapiclientsdk.model.User;
import com.siyufeng.saapiclientsdk.utils.SignUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

import static java.lang.System.currentTimeMillis;

/**
 * 名称api
 *
 * @author 司雨枫
 */
@RestController
@RequestMapping("/name")
public class NameController {
    @GetMapping("/get")
    public String getNameByGet(String name,HttpServletRequest request) {
        System.out.println(request.getHeader("sasa"));
        return "GET 你的名字是" + name;
    }

    @PostMapping("/post")
    public String getNameByPost(@RequestParam String name) {
        return "POST 你的名字是：" + name;
    }

    @PostMapping("/user")
    public String getUserNameByPost(@RequestBody User user, HttpServletRequest request) {
//        String accessKey = request.getHeader("accessKey");
//        String nonce = request.getHeader("nonce");
//        String timestamp = request.getHeader("timestamp");
//        String sign = request.getHeader("sign");
//        String body = request.getHeader("body");
//        //todo 实际情况应该去数据库中查是否已经分配给用户
//        if (!accessKey.equals("syf")) {
//            throw new RuntimeException("parameter is wrong");
//        }
//        if(new Long(nonce) > 10000){
//            throw new RuntimeException("parameter is wrong");
//        }
//        Date nowDate = new Date(currentTimeMillis());
//        Date sendDate = new Date(new Long(timestamp));
//        if(nowDate.getHours() != sendDate.getHours() || nowDate.getMinutes() - sendDate.getMinutes() > 5){
//            throw new RuntimeException("parameter is wrong");
//        }
//
//        //todo 实际情况中是从数据库中查出secretKey
//        String serverSign = SignUtils.getSign(body, "syfnb");
//        if(!sign.equals(serverSign)){
//            throw new RuntimeException("parameter is wrong");
//        }
//

        //调用成功后次数加一
        return "POST 用户名是：" + user.getUserName();

    }


}
