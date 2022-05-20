package com.itheima.stock.service;

import com.itheima.stock.vo.resp.PermissionRespNodeVo;
import com.itheima.stock.vo.resp.R;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author Renhanlu
 * @Date 2022/5/20 23:33
 * @Version 1.0
 */

public interface Permission4Service {

    R<List<PermissionRespNodeVo>> permissionTree();
}
