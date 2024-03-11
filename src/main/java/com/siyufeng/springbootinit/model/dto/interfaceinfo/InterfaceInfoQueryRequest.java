package com.siyufeng.springbootinit.model.dto.interfaceinfo;

import com.siyufeng.springbootinit.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 查询请求
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class InterfaceInfoQueryRequest extends PageRequest implements Serializable {

    /**
     * 主键
     */
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 请求头
     */
    private String requestHeader;

    /**
     * 响应头
     */
    private String responseHeader;

    /**
     * 请求参数
     */
    private String requestParams;

    /**
     * 创建人
     */
    private Long userId;

    /**
     * 请求类型
     */
    private String method;

    /**
     * 接口地址
     */
    private String url;

    /**
     * 接口状态 （0-关闭，1-开启）
     */
    private Integer status;

    /**
     * 描述
     */
    private String description;


    private static final long serialVersionUID = 1L;

}