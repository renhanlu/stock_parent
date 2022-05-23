package com.itheima.stock.mapper;

import com.itheima.stock.common.domain.SysRoleDomain;
import com.itheima.stock.pojo.SysRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
* @author Renhanlu
* @description 针对表【sys_role(角色表)】的数据库操作Mapper
* @createDate 2022-05-08 16:06:52
* @Entity com.itheima.stock.pojo.SysRole
*/
@Mapper
public interface SysRoleMapper {

    int deleteByPrimaryKey(Long id);

    int insert(SysRole record);

    int insertSelective(SysRole record);

    SysRole selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysRole record);

    int updateByPrimaryKey(SysRole record);

    /**
     *
     * @param userId
     * @return
     */
   List<Map> getRoleMsg(@Param("userId") String userId);

    /**
     * 查所有的角色id
     * @return
     */
    List<Map> getRoleIds();

    /**
     * 批量删除用户
     * @param userIds
     * @return
     */
    int deleteUserById(@Param("userIds") List<Long> userIds);



    /**
     * 查询当前角色信息
     * @return
     */
    List<SysRoleDomain> selectUser();

    List<SysRole> selectAll();

    /**
     * 根据用户id查询角色信息
     * @param userId
     * @return
     */
    List<SysRole> getRoleByUserId(@Param("userId") String userId);
}
