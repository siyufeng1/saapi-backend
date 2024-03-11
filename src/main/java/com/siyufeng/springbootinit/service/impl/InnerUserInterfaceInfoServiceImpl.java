package com.siyufeng.springbootinit.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.siyufeng.siapicommon.model.entity.UserInterfaceInfo;
import com.siyufeng.siapicommon.service.InnerUserInterfaceInfoService;
import com.siyufeng.springbootinit.mapper.UserInterfaceInfoMapper;
import com.siyufeng.springbootinit.service.UserInterfaceInfoService;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

/**
 * @author 司雨枫
 */
@DubboService
public class InnerUserInterfaceInfoServiceImpl implements InnerUserInterfaceInfoService {

    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;

    @Resource
    private UserInterfaceInfoMapper userInterfaceInfoMapper;

    @Override
    public int invokeCount(long interfaceInfoId, long userId) {
        return userInterfaceInfoService.invokeCount(interfaceInfoId, userId);
    }

    @Override
    public int invokeCountBiggerThanZero(long interfaceInfoId, long userId) {
        QueryWrapper<UserInterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("interfaceInfoId", interfaceInfoId).and(t -> t.eq("userId", userId));
        UserInterfaceInfo userInterfaceInfo = userInterfaceInfoMapper.selectOne(queryWrapper);
        return userInterfaceInfo.getLeftNum();
    }
}
