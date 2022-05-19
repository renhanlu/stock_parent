package com.itheima.stock.mapper;

import com.itheima.stock.common.domain.*;
import com.itheima.stock.pojo.StockRtInfo;
import org.apache.ibatis.annotations.MapKey;
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
     *统计沪深两市T日(当前股票交易日)每分钟的涨跌停数据
     * @return
     */
    @MapKey("time")
    List<Map> getStockCount(@Param("startTime") Date startTime,
                            @Param("closeTime") Date closeTime,
                             @Param("flag")  Integer flag);

    /**
     * 获取个股分时数据
     * @param curTime
     * @param startTime
     * @param code
     * @return
     */
    List<Stock4MinuteDomain> getStockHour(@Param("curTime") Date curTime,
                                          @Param("startTime") Date startTime,
                                          @Param("code") String code);


    /**
     * 统计当前时间下（精确到分钟），股票在各个涨幅区间的数量
     * @param date 开始时间
     * @return
     */
    List<Map> getStockRateCount(@Param("date") Date date);


    /**
     * 个股日K数据查询
     * @param toDate 开始时间
     * @param date 结束时间
     * @param code 股票
     * @return
     */
    List<Stock4EvrDayDomain> getTimeDay(@Param("toDate") Date toDate,
                                        @Param("date") Date date,
                                        @Param("code") String code);




    /**
     * 个股周K线
     * @param code
     * @return
     */
   List<Map> getIndividualStocksByWeek(@Param("code") String code);

    /**
     *个股最新行情获取
     * @param code 股票编码
     * @param date 时间1
     *
     * @return
     */
    Map getQuotesByHour(@Param("code") String code,
                        @Param("date") Date date);

    int insertStock(@Param("infos") List<StockRtInfo> infos);

    /**
     * 个股最新交易数据
     * @param date 时间
     * @param code 股票代码
     * @return
     */
    List<Map> getStockByNew(@Param("date") Date date,
                            @Param("stockCode") String code);
}
