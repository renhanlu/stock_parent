package com.itheima.stock.mapper;

import com.itheima.stock.pojo.SysUserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author Renhanlu
* @description 针对表【sys_user_role(用户角色表)】的数据库操作Mapper
* @createDate 2022-05-08 16:06:52
* @Entity com.itheima.stock.pojo.SysUserRole
*/
@Mapper
public interface SysUserRoleMapper {

    int deleteByPrimaryKey(Long id);

    int insert(SysUserRole record);

    int insertSelective(SysUserRole record);

    SysUserRole selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysUserRole record);

    int updateByPrimaryKey(SysUserRole record);

    List<String> selectUserRole(@Param("userId") String userId);

    void deleteByUserId(@Param("userId") String userId);

    int insertBatch(@Param("list") List<SysUserRole> list);
}
