package com.siyufeng.springbootinit.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 帖子视图
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@Data
public class InterfaceInfoVO implements Serializable {

    private static final long serialVersionUID = 3341819774860587522L;

    private Long id;

    private String name;

    /**
     * 调用次数
     */
    private Integer totalNum;



}
