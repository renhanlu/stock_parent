package com.itheima.stock.vo.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author Renhanlu
 * @Date 2022/5/22 21:31
 * @Version 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PermissionUpdateReqVo {
    private String id;

    private Integer type;

    private String title;

    private String pid;

    private String url;

    private String name;

    private String icon;

    private String perms;

    private String method;

    private String code;

    private int orderNum;
}
