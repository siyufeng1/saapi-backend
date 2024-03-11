package com.siyufeng.springbootinit.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.siyufeng.siapicommon.model.entity.UserInterfaceInfo;

import java.util.List;

/**
* @author siyufeng
* @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Mapper
* @createDate 2024-03-05 19:42:11
* @Entity generator13.domain.UserInterfaceInfo
*/
public interface UserInterfaceInfoMapper extends BaseMapper<UserInterfaceInfo> {
    //select interfaceInfoId, sum(totalNum) as 'totalNum' from user_interface_info group by interfaceInfoId order by 'totalNum' desc limit 3;
    List<UserInterfaceInfo> listTopInvokeInterfaceInfo();
}




