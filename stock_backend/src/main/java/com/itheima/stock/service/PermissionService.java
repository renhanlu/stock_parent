package com.itheima.stock.service;

import com.itheima.stock.pojo.SysPermission;
import com.itheima.stock.vo.req.PermissionAddReqVo;
import com.itheima.stock.vo.req.PermissionUpdateReqVo;
import com.itheima.stock.vo.resp.PermissionRespNodeTreeVo;
import com.itheima.stock.vo.resp.PermissionRespNodeVo;
import com.itheima.stock.vo.resp.R;
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

    /**
     * 查询所有的权限集合
     * @return
     */
    R<List<SysPermission>> getpermissionList();

    /**
     * 添加权限时回显权限树，仅显示目录和菜单
     * @return
     */
    R<List<PermissionRespNodeTreeVo>> getTreeContent();

    List<PermissionRespNodeTreeVo> getPermissionLevelTree(List<SysPermission> permissions, String parentId, int lavel);

    /**
     * 权限添加按钮
     * @param vo
     * @return
     */
    R<String> addPermission(PermissionAddReqVo vo);

    /**
     * 更新修改
     * @param vo
     * @return
     */
    R<String> updatePermission(PermissionUpdateReqVo vo);

    /**
     * 根据权限Id删除权限
     * @param permissionId
     * @return
     */
    R<String> deletePermission(String permissionId);
}