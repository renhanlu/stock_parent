package com.itheima.stock.service.impl;

import com.itheima.stock.mapper.SysRoleMapper;
import com.itheima.stock.service.RoleService;
import com.itheima.stock.vo.resp.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author Renhanlu
 * @Date 2022/5/18 16:00
 * @Version 1.0
 */
@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private SysRoleMapper sysRoleMapper;

    /**
     * 获取用户具有的角色信息，以及所有的角色信息
     *
     * @param userId
     * @return
     */
    @Override
    public R<Map> getRoleMsg(String userId) {
        List<Map> roleMsg = sysRoleMapper.getRoleMsg(userId);
        List<Map> roleIds = sysRoleMapper.getRoleIds();
        Map<Object, Object> map = new HashMap<>();
        map.put("allRole", roleMsg);
        map.put("ownRoleIds", roleIds);
        return R.ok(map);
    }
}
