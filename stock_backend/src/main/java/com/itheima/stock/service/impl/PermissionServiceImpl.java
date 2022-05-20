package com.itheima.stock.service.impl;

import com.google.common.collect.Lists;
import com.itheima.stock.mapper.SysPermissionMapper;
import com.itheima.stock.pojo.SysPermission;
import com.itheima.stock.service.PermissionService;
import com.itheima.stock.vo.resp.PermissionRespNodeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service("permissionService")
public class PermissionServiceImpl implements PermissionService {
    @Autowired
    private SysPermissionMapper sysPermissionMapper;

/**
     * @param permissions 权限树状集合
     * @param pid 权限父id，顶级权限的pid默认为0
     * @param isOnlyMenuType true:遍历到菜单，  false:遍历到按钮
     * type: 目录1 菜单2 按钮3
     * @return
     */
    @Override
    public List<PermissionRespNodeVo> getTree(List<SysPermission> permissions, String pid, boolean isOnlyMenuType) {
        ArrayList<PermissionRespNodeVo> list = Lists.newArrayList();
        if (CollectionUtils.isEmpty(permissions)) {
            return list;
        }
        for (SysPermission permission : permissions) {
            if (permission.getPid().equals(pid)) {
                if (permission.getType().intValue()!=3 || !isOnlyMenuType) {
                    PermissionRespNodeVo respNodeVo = new PermissionRespNodeVo();
                    respNodeVo.setId(permission.getId());
                    respNodeVo.setTitle(permission.getTitle());
                    respNodeVo.setIcon(permission.getIcon());
                    respNodeVo.setPath(permission.getUrl());
                    respNodeVo.setName(permission.getName());
                    respNodeVo.setChildren(getTree(permissions,permission.getId(),isOnlyMenuType));
                    list.add(respNodeVo);
                }
            }
        }
        return list;
    }

    @Override
    public List<SysPermission> getPermissionAll() {
        return sysPermissionMapper.getPermissionAll();
    }

    /**
     * 根据用户id查询权限集合
     * @param userId
     * @return
     */
    @Override
    public List<SysPermission> getPermissionByUserId(String userId) {
        return sysPermissionMapper.getPermissionByUserId(userId);
    }

}  