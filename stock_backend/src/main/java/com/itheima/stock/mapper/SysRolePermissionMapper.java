package com.itheima.stock.mapper;

import com.itheima.stock.pojo.SysRolePermission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author Renhanlu
* @description 针对表【sys_role_permission(角色权限表)】的数据库操作Mapper
* @createDate 2022-05-08 16:06:52
* @Entity com.itheima.stock.pojo.SysRolePermission
*/
@Mapper
public interface SysRolePermissionMapper {

    int deleteByPrimaryKey(Long id);

    int insert(SysRolePermission record);

    int insertSelective(SysRolePermission record);

    SysRolePermission selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysRolePermission record);

    int updateByPrimaryKey(SysRolePermission record);

    int addRole(@Param("roles") List<SysRolePermission> roles);

    List<String> selectPermissionById(@Param("roleId") String roleId);

    int deleteByRoleId(@Param("id") String id);

    int insertRole(@Param("roleids") List<SysRolePermission> roleids);
}
