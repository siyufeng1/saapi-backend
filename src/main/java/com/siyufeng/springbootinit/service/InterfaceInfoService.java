package com.siyufeng.springbootinit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.siyufeng.siapicommon.model.entity.InterfaceInfo;

/**
* @author siyufeng
* @description 针对表【interface_info(接口信息)】的数据库操作Service
* @createDate 2024-02-29 19:35:58
*/
public interface InterfaceInfoService extends IService<InterfaceInfo> {

    /**
     * 校验
     *
     * @param interfaceInfo
     * @param add
     */
    void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add);

}
