package com.siyufeng.springbootinit.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.siyufeng.saapiclientsdk.client.SaApiClient;
import com.siyufeng.siapicommon.model.entity.InterfaceInfo;
import com.siyufeng.siapicommon.model.entity.User;
import com.siyufeng.springbootinit.annotation.AuthCheck;
import com.siyufeng.springbootinit.common.*;
import com.siyufeng.springbootinit.constant.CommonConstant;
import com.siyufeng.springbootinit.constant.UserConstant;
import com.siyufeng.springbootinit.exception.BusinessException;
import com.siyufeng.springbootinit.exception.ThrowUtils;
import com.siyufeng.springbootinit.mapper.InterfaceInfoMapper;
import com.siyufeng.springbootinit.model.dto.interfaceinfo.InterfaceInfoAddRequest;
import com.siyufeng.springbootinit.model.dto.interfaceinfo.InterfaceInfoInvokeRequest;
import com.siyufeng.springbootinit.model.dto.interfaceinfo.InterfaceInfoQueryRequest;
import com.siyufeng.springbootinit.model.dto.interfaceinfo.InterfaceInfoUpdateRequest;
import com.siyufeng.springbootinit.model.enums.InterfaceInfoStatusEnum;
import com.siyufeng.springbootinit.service.InterfaceInfoService;
import com.siyufeng.springbootinit.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 接口
 *
 */
@RestController
@RequestMapping("/InterfaceInfo")
@Slf4j
public class InterfaceInfoController {

    @Resource
    private InterfaceInfoService interfaceInfoService;

    @Resource
    private UserService userService;

    @Resource
    private InterfaceInfoMapper interfaceInfoMapper;

    @Resource
    private SaApiClient saApiClient;

    // region 增删改查

    /**
     * 创建
     *
     * @param interfaceInfoAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addInterfaceInfo(@RequestBody InterfaceInfoAddRequest interfaceInfoAddRequest, HttpServletRequest request) {
        if (interfaceInfoAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoAddRequest, interfaceInfo);
        // 校验
        interfaceInfoService.validInterfaceInfo(interfaceInfo, true);
        User loginUser = userService.getLoginUser(request);
        interfaceInfo.setUserId(loginUser.getId());
        boolean result = interfaceInfoService.save(interfaceInfo);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        long newInterfaceInfoId = interfaceInfo.getId();
        return ResultUtils.success(newInterfaceInfoId);
    }

    /**
     * 删除
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteInterfaceInfo(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        if (oldInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 仅本人或管理员可删除
        if (!oldInterfaceInfo.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = interfaceInfoService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 更新
     *
     * @param interfaceInfoUpdateRequest
     * @param request
     * @return
     */
    @PostMapping("/update")
    public BaseResponse<Boolean> updateInterfaceInfo(@RequestBody InterfaceInfoUpdateRequest interfaceInfoUpdateRequest,
                                                     HttpServletRequest request) {
        if (interfaceInfoUpdateRequest == null || interfaceInfoUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoUpdateRequest, interfaceInfo);
        // 参数校验
        interfaceInfoService.validInterfaceInfo(interfaceInfo, false);
        User user = userService.getLoginUser(request);
        long id = interfaceInfoUpdateRequest.getId();
        // 判断是否存在
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        if (oldInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 仅本人或管理员可修改
        if (!oldInterfaceInfo.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean result = interfaceInfoService.updateById(interfaceInfo);
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get")
    public BaseResponse<InterfaceInfo> getInterfaceInfoById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = interfaceInfoService.getById(id);
        return ResultUtils.success(interfaceInfo);
    }

    /**
     * 获取列表（仅管理员可使用）
     *
     * @param InterfaceInfoQueryRequest
     * @return
     */
    @AuthCheck(mustRole = "admin")
    @GetMapping("/list")
    public BaseResponse<List<InterfaceInfo>> listInterfaceInfo(InterfaceInfoQueryRequest InterfaceInfoQueryRequest) {
        InterfaceInfo InterfaceInfoQuery = new InterfaceInfo();
        if (InterfaceInfoQueryRequest != null) {
            BeanUtils.copyProperties(InterfaceInfoQueryRequest, InterfaceInfoQuery);
        }
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>(InterfaceInfoQuery);
        List<InterfaceInfo> InterfaceInfoList = interfaceInfoService.list(queryWrapper);
        return ResultUtils.success(InterfaceInfoList);
    }

    /**
     * 分页获取列表
     *
     * @param InterfaceInfoQueryRequest
     * @param request
     * @return
     */
    @GetMapping("/list/page")
    public BaseResponse<Page<InterfaceInfo>> listInterfaceInfoByPage(InterfaceInfoQueryRequest InterfaceInfoQueryRequest, HttpServletRequest request) {
        if (InterfaceInfoQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo InterfaceInfoQuery = new InterfaceInfo();
        BeanUtils.copyProperties(InterfaceInfoQueryRequest, InterfaceInfoQuery);
        long current = InterfaceInfoQueryRequest.getCurrent();
        long size = InterfaceInfoQueryRequest.getPageSize();
        String sortField = InterfaceInfoQueryRequest.getSortField();
        String sortOrder = InterfaceInfoQueryRequest.getSortOrder();
        String description = InterfaceInfoQuery.getDescription();
        // description 需支持模糊搜索
        InterfaceInfoQuery.setDescription(null);
        // 限制爬虫
        if (size > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>(InterfaceInfoQuery);
        QueryWrapper<InterfaceInfo> interfaceInfoQueryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(description), "description", description);
        queryWrapper.orderBy(StringUtils.isNotBlank(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        Page<InterfaceInfo> InterfaceInfoPage = interfaceInfoService.page(new Page<>(current, size), queryWrapper);
        return ResultUtils.success(InterfaceInfoPage);
    }

    /**
     * 发布接口（仅管理员）
     *
     * @param idRequest
     * @return
     */
    @PostMapping("/publish")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> onlineInterfaceInfo(@RequestBody IdRequest idRequest) {
        if (idRequest == null || idRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //判断该接口是否存在
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(idRequest.getId());
        ThrowUtils.throwIf(oldInterfaceInfo == null, ErrorCode.NOT_FOUND_ERROR);
        //判断该接口是否可以调用
        com.siyufeng.saapiclientsdk.model.User user = new com.siyufeng.saapiclientsdk.model.User("syf");
        String userName = saApiClient.getUserNameByPost(user);
        ThrowUtils.throwIf(userName == null, ErrorCode.SYSTEM_ERROR);

        //修改接口状态
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        interfaceInfo.setId(oldInterfaceInfo.getId());
        interfaceInfo.setStatus(InterfaceInfoStatusEnum.ONLINE.getValue());
        boolean result = interfaceInfoService.updateById(interfaceInfo);
        return ResultUtils.success(result);
    }

    /**
     * 下线接口（仅管理员）
     *
     * @param idRequest
     * @return
     */
    @PostMapping("/offline")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> offlineInterfaceInfo(@RequestBody IdRequest idRequest) {
        if (idRequest == null || idRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //判断该接口是否存在
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(idRequest.getId());
        ThrowUtils.throwIf(oldInterfaceInfo == null, ErrorCode.NOT_FOUND_ERROR);

        //修改接口状态
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        interfaceInfo.setId(oldInterfaceInfo.getId());
        interfaceInfo.setStatus(InterfaceInfoStatusEnum.OFFLINE.getValue());
        boolean result = interfaceInfoService.updateById(interfaceInfo);
        return ResultUtils.success(result);
    }


    /**
     * 测试调用
     *
     * @param
     * @return
     */
    @PostMapping("/invoke")
    public BaseResponse<Object> invokeInterfaceInfo(@RequestBody InterfaceInfoInvokeRequest infoInvokeRequest,
                                                      HttpServletRequest request) {
        if (infoInvokeRequest == null || infoInvokeRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //判断该接口是否存在
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(infoInvokeRequest.getId());
        ThrowUtils.throwIf(oldInterfaceInfo == null, ErrorCode.NOT_FOUND_ERROR);
        if(oldInterfaceInfo.getStatus() != InterfaceInfoStatusEnum.ONLINE.getValue()){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"接口已关闭");
        }
        User loginUser = userService.getLoginUser(request);
        String accessKey = loginUser.getAccessKey();
        String secretKey = loginUser.getSecretKey();
        SaApiClient tempClient = new SaApiClient(accessKey, secretKey);
        Gson gson = new Gson();
        com.siyufeng.saapiclientsdk.model.User user = gson.fromJson(infoInvokeRequest.getUserRequestParams(), com.siyufeng.saapiclientsdk.model.User.class);
        //TODO 根据不同地址调用对应接口
        String result = tempClient.getUserNameByPost(user);
        return ResultUtils.success(result);
    }

}



