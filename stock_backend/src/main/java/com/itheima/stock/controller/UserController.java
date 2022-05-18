package com.itheima.stock.controller;

import com.itheima.stock.pojo.UserPage;
import com.itheima.stock.service.UserService;
import com.itheima.stock.vo.req.LoginReqVo;
import com.itheima.stock.vo.req.PageResult;
import com.itheima.stock.vo.req.UserAddReqVo;
import com.itheima.stock.vo.req.UserReqVo;
import com.itheima.stock.vo.resp.LoginRespVo;
import com.itheima.stock.vo.resp.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequestMapping("/api")
@RestController
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 登录
     * @param vo
     * @return
     */
    @PostMapping("/login")
    public R<LoginRespVo> login(@RequestBody LoginReqVo vo){
        return userService.login(vo);
    }

    @GetMapping("/captcha")
    public R<Map> captcha() {
        return userService.captcha();
    }

    /**
     * 条件综合查询用户分页信息，条件包含：分页信息 用户创建日期范围
     * @param userReqVo
     * @return
     */
    @PostMapping("/users")
    public R<PageResult<UserPage>> userSelect(@RequestBody UserReqVo userReqVo) {
        return userService.selectByUser(userReqVo);
    }
    /**
     *添加用户信息
     * @param userAddReqVo
     * @return
     */
    @PostMapping("/user")
    public R AddUser(@RequestBody UserAddReqVo userAddReqVo) {
        return userService.addUser(userAddReqVo);
    }

}
