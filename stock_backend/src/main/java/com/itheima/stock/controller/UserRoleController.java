package com.itheima.stock.controller;

import com.itheima.stock.service.RoleService;
import com.itheima.stock.vo.req.UpdateMsgReqVo;
import com.itheima.stock.vo.resp.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
     *获取用户具有的角色信息，以及所有的角色信息
     * @param userId
     * @return
     */
    @GetMapping("/user/roles/{userId}")
    public R<Map> getRoleMsg(@PathVariable String userId) {
        return roleService.getRoleMsg(userId);
    }
    @PutMapping("/user/roles")
    public R  updateMsg(@RequestBody UpdateMsgReqVo updateMsgReqVo) {
//        return roleService.updateMsg(updateMsgReqVo);
        return null;
    }
}
