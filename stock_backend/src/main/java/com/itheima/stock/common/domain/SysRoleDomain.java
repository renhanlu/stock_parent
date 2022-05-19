package com.itheima.stock.common.domain;

import lombok.Data;

import java.util.Date;

/**
 * @Author Renhanlu
 * @Date 2022/5/19 20:35
 * @Version 1.0
 */
@Data
public class SysRoleDomain {

    private String id;

    /**
     * 角色名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 状态(1:正常0:弃用)
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除(1未删除；0已删除)
     */
    private Integer deleted;
}
