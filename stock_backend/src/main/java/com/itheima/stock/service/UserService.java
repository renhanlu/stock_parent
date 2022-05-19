package com.itheima.stock.service;

import com.itheima.stock.pojo.UserPage;
import com.itheima.stock.vo.req.*;
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

    /**
     * 条件综合查询用户分页信息，条件包含：分页信息 用户创建日期范围
     * @param userReqVo
     * @return
     */
    R<PageResult<UserPage>> selectByUser(UserReqVo userReqVo);

    /**
     *添加用户信息
     * @param userAddReqVo
     * @return
     */
    R<String> addUser(UserAddReqVo userAddReqVo);

    /**
     * 分页查询角色信息
     * @param rolePageReqVo
     * @return
     */
    R<PageResult> selectUser(RolePageReqVo rolePageReqVo);
}
