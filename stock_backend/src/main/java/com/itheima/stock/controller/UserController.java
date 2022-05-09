package com.itheima.stock.controller;

import com.itheima.stock.service.UserService;
import com.itheima.stock.vo.LoginReqVo;
import com.itheima.stock.vo.LoginRespVo;
import com.itheima.stock.vo.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequestMapping("/api")
@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public R<LoginRespVo> login(@RequestBody LoginReqVo vo){
        return userService.login(vo);
    }

    @GetMapping("/captcha")
    public R<Map> captcha() {
        return userService.captcha();
    }
}
