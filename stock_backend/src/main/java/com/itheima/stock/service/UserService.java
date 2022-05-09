package com.itheima.stock.service;

import com.itheima.stock.vo.LoginReqVo;
import com.itheima.stock.vo.LoginRespVo;
import com.itheima.stock.vo.R;

import java.util.Map;

public interface UserService {
//    登录
    R<LoginRespVo> login(LoginReqVo vo);

    //    验证码
    R<Map> captcha();
}
