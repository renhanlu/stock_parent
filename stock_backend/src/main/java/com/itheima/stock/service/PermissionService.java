package com.itheima.stock.service;

import com.itheima.stock.pojo.SysPermission;
import com.itheima.stock.vo.resp.PermissionRespNodeVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PermissionService {
    /**
     * 根据用户id查询用户信息
     * @param userId
     * @return
     */
    List<SysPermission> getPermissionByUserId(@Param("userId") String userId);


    List<PermissionRespNodeVo> getTree(List<SysPermission> permissions, String pid, boolean isOnlyMenuType);

    List<SysPermission> getPermissionAll();

} 