package com.siyufeng.siapicommon.service;

import com.siyufeng.siapicommon.model.entity.UserInterfaceInfo;

/**
 * @author siyufeng
 * @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Service
 * @createDate 2024-03-05 19:42:11
 */
public interface InnerUserInterfaceInfoService{


    /**
     * 调用接口统计
     *
     * @param interfaceInfoId
     * @param userId
     * @return
     */
    int invokeCount(long interfaceInfoId, long userId);


    /**
     * 用户是否还有调用次数
     * @param interfaceInfoId
     * @param userId
     * @return
     */

    int invokeCountBiggerThanZero(long interfaceInfoId, long userId);
}
