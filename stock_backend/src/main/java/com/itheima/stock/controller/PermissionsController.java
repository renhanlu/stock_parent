package com.itheima.stock.controller;

import com.itheima.stock.service.Permission4Service;
import com.itheima.stock.vo.resp.PermissionRespNodeVo;
import com.itheima.stock.vo.resp.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/permissions/tree/all")
    public R<List<PermissionRespNodeVo>> permissionTree() {
        return permission4Service.permissionTree();
    }
}
