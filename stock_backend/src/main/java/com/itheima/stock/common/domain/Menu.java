package com.itheima.stock.common.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author Renhanlu
 * @Date 2022/5/16 0:11
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Menu {
    private String id;

    private String title;

    private String icon;

    private String path;

    private String name;

    private List<Menu> children;
}
