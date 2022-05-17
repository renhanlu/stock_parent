package com.itheima.stock.service;

import com.itheima.stock.vo.req.LoginReqVo;
import com.itheima.stock.vo.resp.LoginRespVo;
import com.itheima.stock.vo.resp.R;

import java.util.Map;

/**
 * @author Renhanlu
 */
public interface UserService {
    /**
     * 登录
     * @param vo
     * @return
     */
    R<LoginRespVo> login(LoginReqVo vo);

    /**
     * 登录验证码
     * @return
     */
    R<Map> captcha();
}
