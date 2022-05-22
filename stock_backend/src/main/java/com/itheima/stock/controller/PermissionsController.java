package com.itheima.stock.controller;

import com.itheima.stock.pojo.SysPermission;
import com.itheima.stock.service.Permission4Service;
import com.itheima.stock.service.PermissionService;
import com.itheima.stock.vo.req.PermissionAddReqVo;
import com.itheima.stock.vo.req.PermissionUpdateReqVo;
import com.itheima.stock.vo.resp.PermissionRespNodeTreeVo;
import com.itheima.stock.vo.resp.PermissionRespNodeVo;
import com.itheima.stock.vo.resp.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author Renhanlu
 * @Date 2022/5/20 23:24
 * @Version 1.0
 */
@RestController
@RequestMapping("/api")
public class PermissionsController {
    @Autowired
    private Permission4Service permission4Service;
    @Autowired
    private PermissionService permissionService;

    @GetMapping("/permissions/tree/all")
    public R<List<PermissionRespNodeVo>> permissionTree() {
        return permission4Service.permissionTree();
    }

    /**
     * 查询所有的权限集合
     * @return
     */
    @GetMapping("/permissions")
    public R<List<SysPermission>>  getpermissionList() {
        return permissionService.getpermissionList();
    }
    /**
     * 添加权限时回显权限树，仅显示目录和菜单
     * @return
     */
    @GetMapping("/permissions/tree")
    public R<List<PermissionRespNodeTreeVo>>  getTree() {
        return permissionService.getTreeContent();
    }

    /**
     * 权限添加按钮
     * @param vo
     * @return
     */
    @PostMapping("/permission")
    public R<String> addPermission(@RequestBody PermissionAddReqVo vo) {
        return permissionService.addPermission(vo);
    }

    /**
     * 更新修改权限
     * @param vo
     * @return
     */
    @PutMapping("/permission")
    public R<String> updatePermission(@RequestBody PermissionUpdateReqVo vo) {
        return permissionService.updatePermission(vo);
    }

    /**
     * 根据权限Id删除权限
     * @param permissionId
     * @return
     */
    @DeleteMapping("/permission/{permissionId}")
    public  R<String> deletePermission(@PathVariable String permissionId) {
        return permissionService.deletePermission(permissionId);
    }

}
