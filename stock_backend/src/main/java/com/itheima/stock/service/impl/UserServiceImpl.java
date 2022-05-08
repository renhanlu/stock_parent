package com.itheima.stock.service.impl;

import com.google.common.base.Strings;
import com.itheima.stock.mapper.SysUserMapper;
import com.itheima.stock.pojo.SysUser;
import com.itheima.stock.service.UserService;
import com.itheima.stock.vo.LoginReqVo;
import com.itheima.stock.vo.LoginRespVo;
import com.itheima.stock.vo.R;
import com.itheima.stock.vo.ResponseCode;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private SysUserMapper sysUserMapper ;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public R<LoginRespVo> login(LoginReqVo vo) {
//1.判断传入的参数的合法性
        if (vo==null || Strings.isNullOrEmpty(vo.getUsername()) || Strings.isNullOrEmpty(vo.getPassword())) {
            //请求参数异常
            return R.error(ResponseCode.DATA_ERROR.getMessage());
        }
        //2.根据用户名查询用户信息（包含了加密后的密文）
        SysUser userInfo=sysUserMapper.getPasswordByname(vo.getUsername());
        //3.判断对应用户是否存在，以及密码是否匹配
        if (userInfo==null || ! passwordEncoder.matches(vo.getPassword(),userInfo.getPassword())) {
            return  R.error(ResponseCode.SYSTEM_PASSWORD_ERROR.getMessage());
        }
        LoginRespVo loginRespVo = new LoginRespVo();

        BeanUtils.copyProperties(userInfo,loginRespVo);
        //5.组装响应对象，响应前端
        return R.ok(loginRespVo);
    }


}
