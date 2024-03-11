package com.siyufeng.siapicommon.service;


import com.siyufeng.siapicommon.model.entity.User;

/**
 * 用户服务
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
public interface InnerUserService{


    /**
     * 数据库中查找是否已经分配给用户密钥
     *
     * @param accessKey
     * @return
     */
    User getInvokeUser(String accessKey);


}
