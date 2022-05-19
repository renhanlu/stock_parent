package com.itheima.stock.service;

import com.itheima.stock.pojo.SysUser;
import com.itheima.stock.vo.req.UpdateMsgReqVo;
import com.itheima.stock.vo.resp.R;
import com.itheima.stock.vo.resp.UserRoleRespVo;

import java.util.List;
import java.util.Map;

/**
 * @Author Renhanlu
 * @Date 2022/5/18 15:59
 * @Version 1.0
 */
public interface RoleService {
    R<UserRoleRespVo> getRoleMsg(String userId);

    R deleteByIds(List<Long> userIds);

    R<Map> selectUserById(Long id);

    /**
     * 根据id更新用户基本信息
     * @param sysUser
     * @return
     */
    R updateUserById(SysUser sysUser);

    R updateMsg(UpdateMsgReqVo updateMsgReqVo);
}
