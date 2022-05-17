package com.itheima.stock.vo.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Menu {
    /**
     * 权限id
     */
    private String id;
    /**
     * 权限标题
     */
    private String title;
    /**
     * 权限图标
     */
    private String icon;
    /**
     * 请求路径
     */
    private String path;
    /**
     * 权限名称
     */
    private String name;
    /**
     * 权限树
     */
    private List<Menu> children;

}