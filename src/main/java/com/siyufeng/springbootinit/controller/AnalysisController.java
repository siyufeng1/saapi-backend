package com.siyufeng.springbootinit.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.siyufeng.siapicommon.model.entity.InterfaceInfo;
import com.siyufeng.siapicommon.model.entity.UserInterfaceInfo;
import com.siyufeng.springbootinit.annotation.AuthCheck;
import com.siyufeng.springbootinit.common.BaseResponse;
import com.siyufeng.springbootinit.common.ErrorCode;
import com.siyufeng.springbootinit.common.ResultUtils;
import com.siyufeng.springbootinit.exception.BusinessException;
import com.siyufeng.springbootinit.mapper.UserInterfaceInfoMapper;
import com.siyufeng.springbootinit.model.vo.InterfaceInfoVO;
import com.siyufeng.springbootinit.service.InterfaceInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 司雨枫
 * 分析控制器
 */
@RestController
@RequestMapping("/analysis")
@Slf4j
public class AnalysisController {

    @Resource
    private InterfaceInfoService interfaceInfoService;

    @Resource
    private UserInterfaceInfoMapper userInterfaceInfoMapper;

    @AuthCheck(mustRole = "admin")
    @GetMapping("/top/interface/invoke")
    public BaseResponse<List<InterfaceInfoVO>> listTopInvokeInterfaceInfo() {
        List<UserInterfaceInfo> userInterfaceInfoList = userInterfaceInfoMapper.listTopInvokeInterfaceInfo();
        List<Long> interfaceIdList = new ArrayList<>();
        for (UserInterfaceInfo userInterfaceInfo : userInterfaceInfoList) {
            interfaceIdList.add(userInterfaceInfo.getInterfaceInfoId());
        }
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", interfaceIdList);
        List<InterfaceInfo> interfaceInfoList = interfaceInfoService.list(queryWrapper);
        if(CollectionUtils.isEmpty(interfaceInfoList)){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        List<InterfaceInfoVO> interfaceInfoVOList = new ArrayList<>();
        for (InterfaceInfo interfaceInfo : interfaceInfoList) {
            InterfaceInfoVO interfaceInfoVO = new InterfaceInfoVO();
            BeanUtils.copyProperties(interfaceInfo, interfaceInfoVO);
            for (UserInterfaceInfo userInterfaceInfo : userInterfaceInfoList) {
                if(userInterfaceInfo.getInterfaceInfoId().equals(interfaceInfoVO.getId())){
                    interfaceInfoVO.setTotalNum(userInterfaceInfo.getTotalNum());
                    break;
                }
            }
            interfaceInfoVOList.add(interfaceInfoVO);
        }
        return ResultUtils.success(interfaceInfoVOList);
    }
}
