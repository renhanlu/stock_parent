package com.itheima.stock.vo.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author by itheima
 * @Date 2021/12/24
 * @Description 登录后响应前端的vo
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRespVo {
    /**
     * 用户ID
     */
    private String id;
    /**
     * 电话
     */
    private String phone;
    /**
     * 用户名
     */
    private String username;
    /**
     * 昵称
     */
    private String nickName;

    private String realName;

    private Integer sex;

    private Integer status;

    private String email;

    /**
     * 权限树（仅仅显示菜单，不是加载按钮信息）
     */
    private List<PermissionRespNodeVo> menus;

    /**
     * 按钮权限集合
     */
    private List<String> permissions;

    /**
     * 响应前端的token字符串
     */
    private String  accessToken;

}
