package com.siyufeng.siapicommon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.siyufeng.siapicommon.model.entity.InterfaceInfo;

import java.util.Map;

/**
* @author siyufeng
* @description 针对表【interface_info(接口信息)】的数据库操作Service
* @createDate 2024-02-29 19:35:58
*/
public interface InnerInterfaceInfoService{

    /**
     * 从数据库中查询模拟接口是否存在：
     *
     * @param url
     * @param method
     * @return
     */
    InterfaceInfo getInterfaceInfo(String url, String method);


}
