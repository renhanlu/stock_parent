package com.itheima.stock.mapper;

import com.itheima.stock.common.domain.StockOutDomain;
import com.itheima.stock.pojo.StockOuterMarketIndexInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
* @author Renhanlu
* @description 针对表【stock_outer_market_index_info(外盘详情信息表)】的数据库操作Mapper
* @createDate 2022-05-08 16:06:52
* @Entity com.itheima.stock.pojo.StockOuterMarketIndexInfo
*/
@Mapper
public interface StockOuterMarketIndexInfoMapper {

    int deleteByPrimaryKey(Long id);

    int insert(StockOuterMarketIndexInfo record);

    int insertSelective(StockOuterMarketIndexInfo record);

    StockOuterMarketIndexInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(StockOuterMarketIndexInfo record);

    int updateByPrimaryKey(StockOuterMarketIndexInfo record);

    List<StockOutDomain> getOutStock(@Param("date") Date date,
                                     @Param("stockIds") List<String> stockIds);

    int insertAll(@Param("list") List<StockOuterMarketIndexInfo> list);
}
