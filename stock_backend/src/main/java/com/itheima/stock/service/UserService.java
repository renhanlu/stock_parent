package com.itheima.stock.service;

import com.itheima.stock.vo.req.LoginReqVo;
import com.itheima.stock.vo.resp.LoginRespVo;
import com.itheima.stock.vo.resp.R;

import java.util.Map;

public interface UserService {
//    登录
    R<LoginRespVo> login(LoginReqVo vo);

    //    验证码
    R<Map> captcha();
}
