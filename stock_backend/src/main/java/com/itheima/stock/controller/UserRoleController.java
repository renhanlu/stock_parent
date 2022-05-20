package com.itheima.stock.controller;

import com.itheima.stock.pojo.SysUser;
import com.itheima.stock.service.RoleService;
import com.itheima.stock.vo.req.UpdateMsgReqVo;
import com.itheima.stock.vo.req.UserRoleReqVo;
import com.itheima.stock.vo.resp.R;
import com.itheima.stock.vo.resp.UserRoleRespVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Author Renhanlu
 * @Date 2022/5/18 15:54
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

}
