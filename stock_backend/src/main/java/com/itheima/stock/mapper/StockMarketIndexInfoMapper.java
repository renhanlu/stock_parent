package com.itheima.stock.mapper;

import com.itheima.stock.common.domain.InnerMarketDomain;
import com.itheima.stock.pojo.StockMarketIndexInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
* @author Renhanlu
* @description 针对表【stock_market_index_info(国内大盘数据详情表)】的数据库操作Mapper
* @createDate 2022-05-08 16:06:52
* @Entity com.itheima.stock.pojo.StockMarketIndexInfo
*/
@Mapper
public interface StockMarketIndexInfoMapper {

    int deleteByPrimaryKey(Long id);

    int insert(StockMarketIndexInfo record);

    int insertSelective(StockMarketIndexInfo record);

    StockMarketIndexInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(StockMarketIndexInfo record);

    int updateByPrimaryKey(StockMarketIndexInfo record);

    List<InnerMarketDomain> getAllByMarket(@Param("marketIds") List<String> marketIds,
                                           @Param("time") Date time);

    /**
     *
     * @param date  今日
     * @param toDate 昨日
     * @param stockIds 大盘Id
     * @return
     */
    List<Map> etStockCompared(@Param("date") Date date,
                              @Param("toDate") Date toDate,
                              @Param("stockIds") List<String> stockIds);

    int insertStock(@Param("list") List<StockMarketIndexInfo> list);
}
