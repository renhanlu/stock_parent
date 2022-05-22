package com.itheima.stock.vo.resp;

import lombok.Data;

/**
 * @Author Renhanlu
 * @Date 2022/5/22 16:27
 * @Version 1.0
 */
@Data
public class PermissionRespNodeTreeVo {
    /**
     * 权限ID
     */
    private String id;
    /**
     * 菜单名称
     */
    private String title;
    /**
     * 菜单等级 1.目录 2.菜单 3.按钮
     */
    private int level;
}
