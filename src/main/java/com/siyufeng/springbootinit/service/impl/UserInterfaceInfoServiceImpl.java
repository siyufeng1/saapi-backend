package com.siyufeng.springbootinit.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.siyufeng.siapicommon.model.entity.UserInterfaceInfo;
import com.siyufeng.springbootinit.common.ErrorCode;
import com.siyufeng.springbootinit.exception.BusinessException;
import com.siyufeng.springbootinit.mapper.UserInterfaceInfoMapper;
import com.siyufeng.springbootinit.service.UserInterfaceInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author siyufeng
 * @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Service实现
 * @createDate 2024-03-05 19:42:11
 */
@Service
public class UserInterfaceInfoServiceImpl extends ServiceImpl<UserInterfaceInfoMapper, UserInterfaceInfo> implements UserInterfaceInfoService {

    @Resource
    private UserInterfaceInfoMapper userInterfaceInfoMapper;

    @Override
    public void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean add) {
        if (userInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 创建时，参数不能为空
        if (add) {
            if (userInterfaceInfo.getId() <= 0 || userInterfaceInfo.getUserId() <= 0) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口或用户不存在");
            }
        }
        // 有参数则校验
        if (userInterfaceInfo.getTotalNum() < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "剩余次数不能小于0");
        }
    }


    @Override
    public int invokeCount(long interfaceInfoId, long userId) {
        //校验
        if (interfaceInfoId <= 0 || userId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口或用户不存在");
        }
        QueryWrapper<UserInterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("interfaceInfoId", interfaceInfoId).and(t -> t.eq("userId", userId)).and(t->t.gt("leftNum", 0));
        UserInterfaceInfo userInterfaceInfo = userInterfaceInfoMapper.selectOne(queryWrapper);
        userInterfaceInfo.setLeftNum(userInterfaceInfo.getLeftNum() - 1);
        userInterfaceInfo.setTotalNum(userInterfaceInfo.getTotalNum() + 1);
        return userInterfaceInfoMapper.updateById(userInterfaceInfo);
    }
}




