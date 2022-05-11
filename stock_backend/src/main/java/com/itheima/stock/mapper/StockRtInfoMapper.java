package com.itheima.stock.mapper;

import com.itheima.stock.common.domain.StockUpdownDomain;
import com.itheima.stock.pojo.StockRtInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
* @author Renhanlu
* @description 针对表【stock_rt_info(个股详情信息表)】的数据库操作Mapper
* @createDate 2022-05-08 16:06:52
* @Entity com.itheima.stock.pojo.StockRtInfo
*/
@Mapper
public interface StockRtInfoMapper {

    int deleteByPrimaryKey(Long id);

    int insert(StockRtInfo record);

    int insertSelective(StockRtInfo record);

    StockRtInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(StockRtInfo record);

    int updateByPrimaryKey(StockRtInfo record);

    List<StockUpdownDomain> getAllTransationData(@Param("datetime") Date datetime);

    /**
     * 分页查询
     */
    List<StockUpdownDomain> getAllByLimt();

    /**
     *
     * @return
     */
    List<Map> getStockCount(@Param("startTime") Date startTime,
                            @Param("closeTime") Date closeTime,
                             @Param("flag")         Integer flag);
}
