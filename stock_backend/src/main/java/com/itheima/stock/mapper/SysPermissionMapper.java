package com.itheima.stock.mapper;

import com.itheima.stock.pojo.SysPermission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author Renhanlu
* @description 针对表【sys_permission(权限表（菜单）)】的数据库操作Mapper
* @createDate 2022-05-08 16:06:52
* @Entity com.itheima.stock.pojo.SysPermission
*/
@Mapper
public interface SysPermissionMapper {

    int deleteByPrimaryKey(Long id);

    int insert(SysPermission record);

    int insertSelective(SysPermission record);

    SysPermission selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysPermission record);

    int updateByPrimaryKey(SysPermission record);

    List<SysPermission> getPermissionByUserId(@Param("userId") String userId);

//    获取所有的权限
    List<SysPermission> getPermissionAll();


    int deleteByPrimaryById(@Param("permissionId") String permissionId);
}
