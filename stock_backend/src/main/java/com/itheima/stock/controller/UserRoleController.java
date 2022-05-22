package com.itheima.stock.controller;

import com.itheima.stock.pojo.SysUser;
import com.itheima.stock.service.RoleService;
import com.itheima.stock.vo.req.UpdateMsgReqVo;
import com.itheima.stock.vo.req.UpdateRoleReqVo;
import com.itheima.stock.vo.req.UserRoleReqVo;
import com.itheima.stock.vo.resp.R;
import com.itheima.stock.vo.resp.UserRoleRespVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Author Renhanlu
 * @Date 2022/5/21 22:54
 * @Version 1.0
 */
@RestController
@RequestMapping("/api")
public class UserRoleController {
    @Autowired
    private RoleService roleService;

    /**
     * 获取用户具有的角色信息，以及所有的角色信息
     *
     * @param userId
     * @return
     */
    @GetMapping("/user/roles/{userId}")
    public R<UserRoleRespVo> getRoleMsg(@PathVariable String userId) {
        return roleService.getRoleMsg(userId);
    }

    @PutMapping("/user/roles")
    public R updateMsg(@RequestBody UpdateMsgReqVo updateMsgReqVo) {
      return roleService.updateMsg(updateMsgReqVo);

    }

    @DeleteMapping("/user")
    public R deleteByIds(@RequestBody List<Long> userIds) {
        return roleService.deleteByIds(userIds);
    }

    /**
     * 根据Id查找用户
     * @param id
     * @return
     */
    @GetMapping("/user/info/{userId}")
    public R<Map> selectUserById(@PathVariable Long id) {
        return roleService.selectUserById(id);
    }

    /**
     * 根据id修改用户
     * @param sysUser
     * @return
     */
    @PutMapping("/user")
    public  R  updateUserById(@RequestBody SysUser sysUser) {
        return roleService.updateUserById(sysUser);
    }

    @PostMapping("/role")
    public R addUserRole(@RequestBody UserRoleReqVo userRoleReqVo) {
        return roleService.addUserRole(userRoleReqVo);
    }

    /**
     * 根据角色id查找权限id
     * @return
     */
    @GetMapping("/role/{roleId}")
    public R<List<String>> selectPermissionById(@PathVariable String roleId) {
        return roleService.selectPermissionById(roleId);
    }

    /**
     * 根据角色id删除角色信息
     * @param roleId
     * @return
     */
    @DeleteMapping("/role/{roleId}")
     public R<String> deleteRoleById(@PathVariable String roleId) {
        return roleService.deleteRoleById(roleId);
    }

    /**
     * 更新角色和角色关联权限
     * @param updateRoleReqVo
     * @return
     */
    @PutMapping("/role")
    public R<String> updateRole(@RequestBody UpdateRoleReqVo updateRoleReqVo) {
        return roleService.updateRole(updateRoleReqVo);
    }

    /**
     * 更新用户的状态信息
     * @return
     */
    @PostMapping("/role/{roleId}/{status}")
    public R<String> updateRoleStatus(@PathVariable String roleId,@PathVariable Integer status) {
        return roleService.updateRoleStatus(roleId,status);
    }
}
