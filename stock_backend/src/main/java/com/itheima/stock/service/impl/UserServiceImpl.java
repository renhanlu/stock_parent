package com.itheima.stock.service.impl;

import com.google.common.base.Strings;
import com.itheima.stock.mapper.SysUserMapper;
import com.itheima.stock.pojo.SysUser;
import com.itheima.stock.service.UserService;
import com.itheima.stock.utils.IdWorker;
import com.itheima.stock.vo.req.LoginReqVo;
import com.itheima.stock.vo.resp.LoginRespVo;
import com.itheima.stock.vo.resp.R;
import com.itheima.stock.vo.resp.ResponseCode;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private IdWorker idWorker;


    @Override
    public R<LoginRespVo> login(LoginReqVo vo) {
//          1.判断传入的参数的合法性
        if (vo == null || Strings.isNullOrEmpty(vo.getUsername()) || Strings.isNullOrEmpty(vo.getPassword())
        || Strings.isNullOrEmpty(vo.getRkey())) {
            //请求参数异常
            return R.error(ResponseCode.DATA_ERROR.getMessage());
        }
        //2.根据用户名查询用户信息（包含了加密后的密文）
        SysUser user = sysUserMapper.getPasswordByname(vo.getUsername());
//        获取Redis缓存的验证码
        String code = (String) redisTemplate.opsForValue().get(vo.getRkey());
//        判断验证码是否为空或者验证码错误
        if (Strings.isNullOrEmpty(code) || !code.equals(vo.getCode())) {
            return R.error(ResponseCode.DATA_ERROR.getMessage());
        }
        //3.判断对应用户是否存在，以及密码是否匹配
        if (user == null || !passwordEncoder.matches(vo.getPassword(), user.getPassword())) {
            return R.error(ResponseCode.SYSTEM_PASSWORD_ERROR.getMessage());
        }
        redisTemplate.delete(vo.getRkey());
        LoginRespVo loginRespVo = new LoginRespVo();
        BeanUtils.copyProperties(user, loginRespVo);
        return R.ok(loginRespVo);
    }

    @Override
    public R<Map> captcha() {
//        1.生成四位数字验证码
        String code = RandomStringUtils.randomNumeric(4);
//        2.生成唯一ID
        String id = String.valueOf(idWorker.nextId());
//        存redis中
        redisTemplate.opsForValue().set(id, code, 60, TimeUnit.SECONDS);
        Map<String, String> map = new HashMap<>();
        map.put("rkey",id);
        map.put("code",code);
        return R.ok(map);
    }


}
