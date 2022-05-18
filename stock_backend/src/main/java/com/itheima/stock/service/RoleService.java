package com.itheima.stock.service;

import com.itheima.stock.vo.resp.R;

import java.util.Map;

/**
 * @Author Renhanlu
 * @Date 2022/5/18 15:59
 * @Version 1.0
 */
public interface RoleService {
    R<Map> getRoleMsg(String userId);
}
