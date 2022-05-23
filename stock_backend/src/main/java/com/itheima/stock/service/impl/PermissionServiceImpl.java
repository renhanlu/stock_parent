package com.itheima.stock.service.impl;

import com.google.common.collect.Lists;
import com.itheima.stock.mapper.SysPermissionMapper;
import com.itheima.stock.pojo.SysPermission;
import com.itheima.stock.service.PermissionService;
import com.itheima.stock.utils.IdWorker;
import com.itheima.stock.vo.req.PermissionAddReqVo;
import com.itheima.stock.vo.req.PermissionUpdateReqVo;
import com.itheima.stock.vo.resp.PermissionRespNodeTreeVo;
import com.itheima.stock.vo.resp.PermissionRespNodeVo;
import com.itheima.stock.vo.resp.R;
import com.itheima.stock.vo.resp.ResponseCode;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("permissionService")
public class PermissionServiceImpl implements PermissionService {
    @Autowired
    private SysPermissionMapper sysPermissionMapper;
    @Autowired
    private IdWorker idWorker;

    /**
     * @param permissions    权限树状集合
     * @param pid            权限父id，顶级权限的pid默认为0
     * @param isOnlyMenuType true:遍历到菜单，  false:遍历到按钮
     *                       type: 目录1 菜单2 按钮3
     * @return
     */
    @Override
    public List<PermissionRespNodeVo> getTree(List<SysPermission> permissions, String pid, boolean isOnlyMenuType) {
        ArrayList<PermissionRespNodeVo> list = Lists.newArrayList();
        if (CollectionUtils.isEmpty(permissions)) {
            return list;
        }
        for (SysPermission permission : permissions) {
            if (permission.getPid().equals(pid)) {
                if (permission.getType().intValue() != 3 || !isOnlyMenuType) {
                    PermissionRespNodeVo respNodeVo = new PermissionRespNodeVo();
                    respNodeVo.setId(permission.getId());
                    respNodeVo.setTitle(permission.getTitle());
                    respNodeVo.setIcon(permission.getIcon());
                    respNodeVo.setPath(permission.getUrl());
                    respNodeVo.setName(permission.getName());
                    respNodeVo.setChildren(getTree(permissions, permission.getId(), isOnlyMenuType));
                    list.add(respNodeVo);
                }
            }
        }
        return list;
    }

    @Override
    public List<SysPermission> getPermissionAll() {
        return sysPermissionMapper.getPermissionAll();
    }

    @Override
    public R<List<SysPermission>> getpermissionList() {
        List<SysPermission> permissionAll = sysPermissionMapper.getPermissionAll();
        return R.ok(permissionAll);
    }

    @Override
    public R<List<PermissionRespNodeTreeVo>> getTreeContent() {
        //获取所有权限集合
        List<SysPermission> all = this.sysPermissionMapper.getPermissionAll();
        //构建权限树集合
        List<PermissionRespNodeTreeVo> result = new ArrayList<>();
        //构架顶级菜单（默认选项）
        PermissionRespNodeTreeVo root = new PermissionRespNodeTreeVo();
        root.setId("0");
        root.setTitle("顶级菜单");
        root.setLevel(0);
        result.add(root);
        result.addAll(getPermissionLevelTree(all, "0", 1));
        return R.ok(result);
    }

    /**
     * 根据用户id查询权限集合
     *
     * @param userId
     * @return
     */
    @Override
    public List<SysPermission> getPermissionByUserId(String userId) {
        return sysPermissionMapper.getPermissionByUserId(userId);
    }

    @Override
    public List<PermissionRespNodeTreeVo> getPermissionLevelTree(List<SysPermission> permissions, String parentId, int lavel) {
        List<PermissionRespNodeTreeVo> result = new ArrayList<>();
        for (SysPermission permission : permissions) {
            if (permission.getType().intValue() != 3 && permission.getPid().equals(parentId)) {
                PermissionRespNodeTreeVo nodeTreeVo = new PermissionRespNodeTreeVo();
                nodeTreeVo.setId(permission.getId());
                nodeTreeVo.setTitle(permission.getTitle());
                nodeTreeVo.setLevel(lavel);
                result.add(nodeTreeVo);
                result.addAll(getPermissionLevelTree(permissions, permission.getId(), lavel + 1));
            }
        }
        return result;
    }

    /**
     * 权限添加按钮
     *
     * @param vo
     * @return
     */
    @Override
    public R<String> addPermission(PermissionAddReqVo vo) {
        SysPermission permission = new SysPermission();
        BeanUtils.copyProperties(vo, permission);
        permission.setStatus(1);
        permission.setCreateTime(new Date());
        permission.setUpdateTime(new Date());
        permission.setDeleted(1);
        permission.setId(idWorker.nextId() + "");
        //插入数据库
        int count = this.sysPermissionMapper.insert(permission);
/*        SysPermission sysPermission = SysPermission.builder().id(idWorker.nextId()+"").type(vo.getType()).title(vo.getTitle())
                .pid(vo.getPid()).url(vo.getUrl()).name(vo.getName())
                .icon(vo.getIcon()).perms(vo.getPerms()).method(vo.getMethod())
                .code(vo.getCode()).orderNum(vo.getOrderNum()).build();
        int count= sysPermissionMapper.insertSelective(sysPermission);
        if (count==0) {
            return R.error(ResponseCode.ERROR.getMessage());
        }
        */
        return R.ok(ResponseCode.SUCCESS.getMessage());
    }

    /**
     * 更新修改
     *
     * @param vo
     * @return
     */
    @Override
    public R<String> updatePermission(PermissionUpdateReqVo vo) {
        SysPermission sysPermission = SysPermission.builder().id(vo.getId()).name(vo.getName())
                .type(vo.getType()).title(vo.getTitle()).pid(vo.getPid())
                .url(vo.getUrl()).icon(vo.getIcon()).perms(vo.getPerms())
                .method(vo.getMethod()).code(vo.getCode()).orderNum(vo.getOrderNum()).build();
        sysPermissionMapper.updateByPrimaryKeySelective(sysPermission);
        return R.ok(ResponseCode.SUCCESS.getMessage());
    }

    /**
     * 根据权限Id删除权限
     *
     * @param permissionId
     * @return
     */
    @Override
    public R<String> deletePermission(String permissionId) {
        int i = sysPermissionMapper.deleteByPrimaryById(permissionId);
        SysPermission permission = SysPermission.builder().id(permissionId).deleted(0).updateTime(new Date()).build();
        int updateCount = this.sysPermissionMapper.updateByPrimaryKeySelective(permission);
        return R.ok(ResponseCode.SUCCESS.getMessage());
    }

}  