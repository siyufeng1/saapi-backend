package com.siyufeng.springbootinit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.siyufeng.siapicommon.model.entity.UserInterfaceInfo;

/**
 * @author siyufeng
 * @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Service
 * @createDate 2024-03-05 19:42:11
 */
public interface UserInterfaceInfoService extends IService<UserInterfaceInfo> {
    /**
     * 校验
     *
     * @param userInterfaceInfo
     * @param add
     */
    void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean add);


    /**
     * 调用接口统计
     *
     * @param interfaceInfoId
     * @param userId
     * @return
     */
    int invokeCount(long interfaceInfoId, long userId);
}
