package com.itheima.stock.service;

import com.itheima.stock.vo.LoginReqVo;
import com.itheima.stock.vo.LoginRespVo;
import com.itheima.stock.vo.R;

public interface UserService {
    R<LoginRespVo> login(LoginReqVo vo);
}
