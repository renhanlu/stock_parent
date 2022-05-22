package com.itheima.stock.service.impl;

import com.itheima.stock.mapper.SysRoleMapper;
import com.itheima.stock.mapper.SysRolePermissionMapper;
import com.itheima.stock.mapper.SysUserMapper;
import com.itheima.stock.mapper.SysUserRoleMapper;
import com.itheima.stock.pojo.SysRole;
import com.itheima.stock.pojo.SysRolePermission;
import com.itheima.stock.pojo.SysUser;
import com.itheima.stock.pojo.SysUserRole;
import com.itheima.stock.service.RoleService;
import com.itheima.stock.utils.IdWorker;
import com.itheima.stock.vo.req.UpdateMsgReqVo;
import com.itheima.stock.vo.req.UpdateRoleReqVo;
import com.itheima.stock.vo.req.UserRoleReqVo;
import com.itheima.stock.vo.resp.R;
import com.itheima.stock.vo.resp.ResponseCode;
import com.itheima.stock.vo.resp.UserRoleRespVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author Renhanlu
 * @Date 2022/5/18 16:00
 * @Version 1.0
 */
@Service
@Slf4j
public class RoleServiceImpl implements RoleService {
    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private SysRolePermissionMapper sysRolePermissionMapper;


    /**
     * 获取用户具有的角色信息，以及所有的角色信息
     *
     * @param userId
     * @return
     */
    @Override
    public R<UserRoleRespVo> getRoleMsg(String userId) {
        List<String> role = sysUserRoleMapper.selectUserRole(userId);
        List<SysRole> roles = sysRoleMapper.selectAll();
        UserRoleRespVo userRoleRespVo = new UserRoleRespVo();
        userRoleRespVo.setAllRole(roles);
        userRoleRespVo.setOwnRoleIds(role);

        return R.ok(userRoleRespVo);
    }

    /**
     * 批量删除用户信息
     *
     * @param userIds
     * @return
     */
    @Override
    public R deleteByIds(List<Long> userIds) {
        int count = sysRoleMapper.deleteUserById(userIds);
        if (count == 0) {
            return R.error("删除失败");
        }
        log.info("删除数量{}", count);
        return R.ok();
    }

    /**
     * 根据id查找用户
     *
     * @param id
     * @return
     */
    @Override
    public R<Map> selectUserById(Long id) {
        Map users = sysUserMapper.selectByUserId(id);
        if (CollectionUtils.isEmpty(users)) {
            return R.error("没有此用户");
        }
        return R.ok(users);
    }

    /**
     * 根据id更新用户基本信息
     *
     * @param sysUser
     * @return
     */
    @Override
    public R updateUserById(SysUser sysUser) {
        int count = sysUserMapper.updateByPrimaryKeySelective(sysUser);
        if (count == 0) {
            return R.error();
        }
        return R.ok();
    }

    @Override
    public R updateMsg(UpdateMsgReqVo updateMsgReqVo) {
        //1.判断用户id是否存在
        if (updateMsgReqVo.getUserId() == null) {
            return R.error(ResponseCode.ERROR.getMessage());
        }
        //2.删除用户原来所拥有的角色id
        this.sysUserRoleMapper.deleteByUserId(updateMsgReqVo.getUserId());
        //如果对应集合为空，则说明用户将所有角色都清除了
        if (CollectionUtils.isEmpty(updateMsgReqVo.getRoleIds())) {
            return R.ok(ResponseCode.SUCCESS.getMessage());
        }
        List<SysUserRole> list = updateMsgReqVo.getRoleIds().stream().map(roleId -> {
            SysUserRole userRole = SysUserRole.builder().
                    userId(updateMsgReqVo.getUserId()).roleId(roleId).
                    createTime(new Date()).id(idWorker.nextId() + "").build();
            return userRole;
        }).collect(Collectors.toList());
        //批量插入
        int count = this.sysUserRoleMapper.insertBatch(list);
        if (count == 0) {
            return R.error(ResponseCode.ERROR.getMessage());
        }
        return R.ok(ResponseCode.SUCCESS.getMessage());
    }

    /**
     * 添加角色和角色关联权限
     *
     * @param userRoleReqVo
     * @return
     */
    @Override
    public R addUserRole(UserRoleReqVo userRoleReqVo) {
        Long roleId = idWorker.nextId();
        SysRole role = SysRole.builder().id(roleId + "")
                .deleted(1).name(userRoleReqVo.getName()).description(userRoleReqVo.getDescription())
                .createTime(new Date()).updateTime(new Date()).status(1).build();
        //添加角色
        int addRoleCount = sysRoleMapper.insert(role);
        if (addRoleCount != 1) {
            R.error(ResponseCode.ERROR.getMessage());
        }
        //2.批量添加角色关联权限集合
        List<String> permissionsIds = userRoleReqVo.getPermissionsIds();
        if (!CollectionUtils.isEmpty(permissionsIds)) {
            List<SysRolePermission> rps = permissionsIds.stream().map(permissionId -> {
                SysRolePermission rp = SysRolePermission.builder().id(idWorker.nextId() + "").roleId(roleId + "")
                        .permissionId(permissionId).createTime(new Date()).build();
                return rp;
            }).collect(Collectors.toList());
            //批量插入角色权限集合
            int counts = this.sysRolePermissionMapper.addRole(rps);
            if (counts == 0) {
                R.error(ResponseCode.ERROR.getMessage());
            }
        }
        return R.ok(ResponseCode.SUCCESS.getMessage());
    }

    @Override
    public R<List<String>> selectPermissionById(String roleId) {
        List<String> roleIds = sysRolePermissionMapper.selectPermissionById(roleId);
        return R.ok(roleIds);
    }

    /**
     * 根据角色id删除角色信息
     *
     * @param roleId
     * @return
     */
    @Override
    public R<String> deleteRoleById(String roleId) {
        int count = sysRoleMapper.deleteByPrimaryKey(Long.valueOf(roleId));
        if (count == 0) {
            return R.error(ResponseCode.ERROR.getMessage());
        }
        return R.ok(ResponseCode.SUCCESS.getMessage());
    }

    /**
     * 更新角色和角色关联权限
     *
     * @param updateRoleReqVo
     * @return
     */
    @Override
    public R<String> updateRole(UpdateRoleReqVo updateRoleReqVo) {

        SysRole sysRole = SysRole.builder().updateTime(new Date()).deleted(1)
                .description(updateRoleReqVo.getDescription()).name(updateRoleReqVo.getName())
                .build();
        sysRoleMapper.updateByPrimaryKeySelective(sysRole);
        List<String> permissionsIds = updateRoleReqVo.getPermissionsIds();
//        先删除，后添加
        sysRolePermissionMapper.deleteByRoleId(updateRoleReqVo.getId());
        if (!CollectionUtils.isEmpty(permissionsIds)) {
            List<SysRolePermission> rps = permissionsIds.stream().map(permissionId -> {
                SysRolePermission rp = SysRolePermission.builder().id(idWorker.nextId() + "").roleId(updateRoleReqVo.getId())
                        .permissionId(permissionId).createTime(new Date()).build();
                return rp;
            }).collect(Collectors.toList());
            int count = sysRolePermissionMapper.insertRole(rps);

            if (count == 0) {
                return R.error(ResponseCode.ERROR.getMessage());
            }
        }
        return R.ok(ResponseCode.SUCCESS.getMessage());
    }

    /**
     * 更新用户的状态信息
     *
     * @return
     */
    @Override
    public R<String> updateRoleStatus(String roleId, Integer status) {
        SysRole sysRole = SysRole.builder().updateTime(new Date()).status(status).id(roleId).build();
        int i = sysRoleMapper.updateByPrimaryKeySelective(sysRole);
        if (i == 0) {
            return R.error(ResponseCode.ERROR.getMessage());
        }
        return R.ok(ResponseCode.SUCCESS.getMessage());
    }

}
