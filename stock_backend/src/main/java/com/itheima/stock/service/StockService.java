package com.itheima.stock.service;

import com.itheima.stock.common.domain.*;
import com.itheima.stock.vo.req.PageResult;
import com.itheima.stock.vo.resp.R;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @author Renhanlu
 */
public interface StockService {

    /**
     * 查询国内大盘指数
     * @return
     */
    R<List<InnerMarketDomain>> getMarketAll();

    /*
     查询国内板块指数
    * */
    R<List<StockBlockDomain>> getInlandMarket();

    /*
     * 查询沪深两市最新交易信息
     * */

    R<List<StockUpdownDomain>> getLatest();

    /*
     * 分页查询
     * */

    /**
     * @param page
     * @param pageSize
     * @return
     */
    R<PageResult<StockUpdownDomain>> getbyPage(Integer page, Integer pageSize);


    R<Map> getStockCount();

    /**
     * 导出excel数据
     *
     * @param response
     * @param page
     * @param pageSize
     */
    void stockExport(HttpServletResponse response, Integer page, Integer pageSize);

    /**
     * 获取外国大盘数据
     * @return
     */
    R<List<StockOutDomain>> getOutStock();

    /**
     * 统计国内A股大盘T日和T-1日成交量对比功能（成交量为沪市和深市成交量之和）
     * @return
     */

    R<Map> getStockCompared();

    /**
     * 查询个股的分时行情数据
     * @param code
     * @return
     */

    R<List<Stock4MinuteDomain>> getStockHour(String code);


    /**
     * 统计当前时间下（精确到分钟），股票在各个涨幅区间的数量
     * @return
     */
    R<Map> getStockRateCount();

    /**
     * 分天K线
     * @param code
     * @return
     */
    R<List<Stock4EvrDayDomain>> getTimeDay(String code);

    /**
     * 模糊查询股票信息
     * @param searchStr
     * @return
     */
    R<List<StockSearchDomain>> selectStockByLike(String searchStr);


    /**
     * 个股主营业务查询
     * @param code
     * @return
     */
    R<StockBusinessDomain> getIndividualStocks(String code);

    /**
     * 个股周K线
     * @param code
     * @return
     */
    R<List<Map>> getIndividualStocksByWeek(String code);

    /**
     * 个股分时行情获取
     * @param code
     * @return
     */
    R<Map> getQuotesByHour(String code);

    /**
     * 个股最新交易流水数据查询
     * @param code 股票编码
     * @return
     */
    R<List<Map>> getStockByNew(String code);
}

