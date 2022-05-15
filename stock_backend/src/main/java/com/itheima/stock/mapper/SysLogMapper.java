package com.itheima.stock.mapper;

import com.itheima.stock.pojo.SysLog;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Renhanlu
* @description 针对表【sys_log(系统日志)】的数据库操作Mapper
* @createDate 2022-05-08 16:06:52
* @Entity com.itheima.stock.pojo.SysLog
*/
@Mapper
public interface SysLogMapper {

    int deleteByPrimaryKey(Long id);

    int insert(SysLog record);

    int insertSelective(SysLog record);

    SysLog selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysLog record);

    int updateByPrimaryKey(SysLog record);

}
