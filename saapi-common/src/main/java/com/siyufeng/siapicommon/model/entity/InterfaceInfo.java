package com.siyufeng.siapicommon.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * 接口信息
 * @TableName interface_info
 */
@TableName(value ="interface_info")
@Data
public class InterfaceInfo implements Serializable {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
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

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除(0-未删, 1-已删)
     */
    @TableLogic
    private Integer isDeleted;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InterfaceInfo)) return false;
        InterfaceInfo that = (InterfaceInfo) o;
        return getId().equals(that.getId()) && getName().equals(that.getName()) && getRequestHeader().equals(that.getRequestHeader()) && getResponseHeader().equals(that.getResponseHeader()) && getRequestParams().equals(that.getRequestParams()) && getUserId().equals(that.getUserId()) && getMethod().equals(that.getMethod()) && getUrl().equals(that.getUrl()) && getStatus().equals(that.getStatus()) && getDescription().equals(that.getDescription()) && getCreateTime().equals(that.getCreateTime()) && getUpdateTime().equals(that.getUpdateTime()) && getIsDeleted().equals(that.getIsDeleted());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getRequestHeader(), getResponseHeader(), getRequestParams(), getUserId(), getMethod(), getUrl(), getStatus(), getDescription(), getCreateTime(), getUpdateTime(), getIsDeleted());
    }

    @Override
    public String toString() {
        return "InterfaceInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", requestHeader='" + requestHeader + '\'' +
                ", responseHeader='" + responseHeader + '\'' +
                ", requestParams='" + requestParams + '\'' +
                ", userId=" + userId +
                ", method='" + method + '\'' +
                ", url='" + url + '\'' +
                ", status=" + status +
                ", description='" + description + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", isDeleted=" + isDeleted +
                '}';
    }
}