package com.itheima.stock.mapper;

import com.itheima.stock.pojo.SysUser;
import com.itheima.stock.pojo.UserPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
* @author Renhanlu
* @description 针对表【sys_user(用户表)】的数据库操作Mapper
* @createDate 2022-05-08 16:06:52
* @Entity com.itheima.stock.pojo.SysUser
*/
@Mapper
public interface SysUserMapper {

    int deleteByPrimaryKey(Long id);

    int insert(SysUser record);

    int insertSelective(SysUser record);

    SysUser selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysUser record);

    int updateByPrimaryKey(SysUser record);

    SysUser getPasswordByname(@Param("userName") String username);




    /**
     * 多条件综合查询用户分页信息，条件包含：分页信息 用户创建日期范围
     * @return
     */
    List<UserPage> selectByPageAllUser(@Param("userName") String userName, @Param("nickName") String nickName,
                                       @Param("startTime") String startTime, @Param("endTime") String endTime);

    Map selectByUserId(@Param("id") Long id);


}
