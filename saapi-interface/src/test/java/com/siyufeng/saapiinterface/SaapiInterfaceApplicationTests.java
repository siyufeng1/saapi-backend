package com.siyufeng.saapiinterface;


import cn.hutool.json.JSONUtil;
import com.siyufeng.saapiclientsdk.client.SaApiClient;
import com.siyufeng.saapiclientsdk.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import javax.annotation.Resources;
import java.text.SimpleDateFormat;
import java.util.Date;

import static java.lang.System.currentTimeMillis;

@SpringBootTest
class SaapiInterfaceApplicationTests {

    @Resource
    private SaApiClient saApiClient;

    @Test
    void  contextLoads() {
//        saApiClient.getNameByGet("syf");
        User syf = new User("syf");
//        saApiClient.getUserNameByPost(syf);
        String s = JSONUtil.toJsonStr(syf);
        System.out.println(s);
    }

}
