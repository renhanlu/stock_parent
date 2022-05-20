package com.itheima.stock.service.impl;

import com.itheima.stock.pojo.SysPermission;
import com.itheima.stock.service.Permission4Service;
import com.itheima.stock.service.PermissionService;
import com.itheima.stock.vo.resp.PermissionRespNodeVo;
import com.itheima.stock.vo.resp.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author Renhanlu
 * @Date 2022/5/20 23:35
 * @Version 1.0
 */
@Service
public class Permission4ServiceImpl implements Permission4Service {
    @Autowired
    private PermissionService permissionService;



    @Override
    public R<List<PermissionRespNodeVo>> permissionTree() {
        //获所有的的权限集合
        List<SysPermission> permissions = permissionService.getPermissionAll();
        //获取树状权限菜单数据
        List<PermissionRespNodeVo> tree = permissionService.getTree(permissions, "0", true);
        return R.ok(tree);
    }
}
