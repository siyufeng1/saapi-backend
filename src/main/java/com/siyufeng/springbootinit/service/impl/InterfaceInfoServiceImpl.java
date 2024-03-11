package com.siyufeng.springbootinit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.siyufeng.siapicommon.model.entity.InterfaceInfo;
import com.siyufeng.springbootinit.common.ErrorCode;
import com.siyufeng.springbootinit.exception.BusinessException;
import com.siyufeng.springbootinit.exception.ThrowUtils;
import com.siyufeng.springbootinit.mapper.InterfaceInfoMapper;
import com.siyufeng.springbootinit.service.InterfaceInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @author siyufeng
 * @description 针对表【interface_info(接口信息)】的数据库操作Service实现
 * @createDate 2024-02-29 19:35:58
 */
@Service
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo>
        implements InterfaceInfoService {

    @Override
    public void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add) {
        if (interfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        String name = interfaceInfo.getName();

        // 创建时，参数不能为空
        if (add) {
            ThrowUtils.throwIf(StringUtils.isAnyBlank(name), ErrorCode.PARAMS_ERROR);
        }
        // 有参数则校验
        if (StringUtils.isNotBlank(name) && name.length() > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "名称过长");
        }
    }
}




