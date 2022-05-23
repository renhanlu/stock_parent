package com.itheima.stock.security.service;

import com.google.common.base.Strings;
import com.itheima.stock.mapper.SysPermissionMapper;
import com.itheima.stock.mapper.SysRoleMapper;
import com.itheima.stock.mapper.SysUserMapper;
import com.itheima.stock.pojo.SysPermission;
import com.itheima.stock.pojo.SysRole;
import com.itheima.stock.pojo.SysUser;
import com.itheima.stock.service.PermissionService;
import com.itheima.stock.vo.resp.PermissionRespNodeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author by itheima
 * @Date 2021/12/24
 * @Description
 */
@Component
public class LoginUserDetailService implements UserDetailsService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysPermissionMapper sysPermissionMapper;

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private PermissionService permissionService;

    /**
     * 根据用户名获取用户详情
     * @param userName 用户名
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        SysUser user= this.sysUserMapper.getPasswordByname(userName);
        if (user==null) {
            throw new UsernameNotFoundException("用户不存在！");
        }
        //获取权限集合
        List<SysPermission> permissionList=this.sysPermissionMapper.getPermissionByUserId(user.getId());
        List<String> permsNameList = permissionList.stream().filter(item -> !Strings.isNullOrEmpty(item.getPerms())).map(item -> item.getPerms())
                .collect(Collectors.toList());

        //获取角色集合 基于角色鉴权注解需要将角色前追加ROLE_
        List<SysRole> roleList= sysRoleMapper.getRoleByUserId(user.getId());
        List<String> roleNameList = roleList.stream().filter(item -> !Strings.isNullOrEmpty(item.getName()))
                .map(item ->  "ROLE_" + item.getName()).collect(Collectors.toList());

        List<String> auths= new ArrayList<String>();
        auths.addAll(permsNameList);
        auths.addAll(roleNameList);

        //转化为数组
        String[] perms=auths.toArray(new String[auths.size()]);

        //转化为数组，给springSecurity的
        List<GrantedAuthority> authorityList = AuthorityUtils.createAuthorityList(perms);
        user.setAuthorities(authorityList);

        //权限树结构，给前端响应
        List<PermissionRespNodeVo> treeNodeVo = permissionService.getTree(permissionList, "0", true);
        user.setMenus(treeNodeVo);

        //按钮权限集合，给前端响应
        List<String> authBtnPerms=null;
        if (!CollectionUtils.isEmpty(permissionList)) {
            authBtnPerms = permissionList.stream().filter(per -> !Strings.isNullOrEmpty(per.getCode()) && per.getType()==3)
                    .map(per -> per.getCode()).collect(Collectors.toList());
        }
        user.setPermissions(authBtnPerms);
        //后续将
        return user;
    }
}
