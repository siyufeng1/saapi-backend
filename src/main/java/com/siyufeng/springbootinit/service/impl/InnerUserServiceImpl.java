package com.siyufeng.springbootinit.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.siyufeng.siapicommon.model.entity.User;
import com.siyufeng.siapicommon.service.InnerUserService;
import com.siyufeng.springbootinit.common.ErrorCode;
import com.siyufeng.springbootinit.exception.BusinessException;
import com.siyufeng.springbootinit.mapper.UserMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

/**
 * @author 司雨枫
 */
@DubboService
public class InnerUserServiceImpl implements InnerUserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public User getInvokeUser(String accessKey) {
        if(StringUtils.isAnyBlank(accessKey)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("accessKey", accessKey);
        User user = userMapper.selectOne(queryWrapper);
        if(user == null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return user;
    }
}
