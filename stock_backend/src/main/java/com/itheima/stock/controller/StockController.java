package com.itheima.stock.controller;

import com.itheima.stock.common.domain.*;
import com.itheima.stock.service.StockService;
import com.itheima.stock.vo.req.PageResult;
import com.itheima.stock.vo.resp.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @author Renhanlu
 */
@RestController
@RequestMapping("/api/quot")
@CrossOrigin
public class StockController {

    @Autowired
    private StockService stockService;


    //    获取国内数据
    @GetMapping("/index/all")
    public R<List<InnerMarketDomain>> getMarketAll() {
        return stockService.getMarketAll();
    }

    //  获取国内板块数据
    @GetMapping("/sector/all")
    public R<List<StockBlockDomain>> getAllinLand() {
        return stockService.getInlandMarket();
    }

    //    统计沪深两市最新交易数据
    @GetMapping("/stock/increase")
    public R<List<StockUpdownDomain>> getLatestDeals() {
        return stockService.getLatest();
    }

    /**
     * 查询沪深两市个股行情 ，以时间顺序和涨幅分页查询
     *
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/stock/all")
    public R<PageResult<StockUpdownDomain>> getDealsByLimit(Integer page, Integer pageSize) {
        return stockService.getbyPage(page, pageSize);
    }

    /**
     * 统计沪深两市涨停跌停数量
     *
     * @return
     */
    @GetMapping("/stock/updown/count")
    public R<Map> getStockCount() {
        return stockService.getStockCount();
    }

    /**
     * 国内大盘信息导出
     *
     * @param response
     * @param page
     * @param pageSize
     */
    @GetMapping("/stock/export")
    public void setFile(HttpServletResponse response, Integer page, Integer pageSize) {
        stockService.stockExport(response, page, pageSize);
    }

    /**
     * 统计国内A股大盘T日和T-1日成交量对比功能（成交量为沪市和深市成交量之和）
     *
     * @return
     */
    @GetMapping("/stock/tradevol")
    public R<Map> getStockCompared() {
        return stockService.getStockCompared();
    }

    /**
     * 查询个股分时K线数据
     *
     * @param code
     * @return
     */

    @GetMapping("/stock/screen/time-sharing")
    public R<List<Stock4MinuteDomain>> getStockHour(String code) {
        return stockService.getStockHour(code);
    }

    /**
     * 统计当前时间下（精确到分钟），股票在各个涨幅区间的数量
     *
     * @return
     */
    @GetMapping("/stock/updown")
    public R<Map> getStockRateCount() {
        return stockService.getStockRateCount();
    }

    /**
     * 个股日K线 根据时间区间查询
     *
     * @param code
     * @return
     */
    @GetMapping("/stock/screen/dkline")
    public R<List<Stock4EvrDayDomain>> getTimeDay(String code) {
        return stockService.getTimeDay(code);
    }


    /**
     * 查询国外大盘数据
     *
     * @return
     */

    @GetMapping("/external/index")
    public R<List<StockOutDomain>> getOutStock() {
        return stockService.getOutStock();
    }

    /**
     * 股票模糊查询 只支持代码 不支持名称
     *
     * @param searchStr
     * @return
     */

    @GetMapping("/stock/search")
    public R<List<StockSearchDomain>> selectStockByLike(String searchStr) {
        return stockService.selectStockByLike(searchStr);
    }

    /**
     * 个股主营业务查询
     *
     * @param code
     * @return
     */
    @GetMapping("/stock/describe")
    public R<StockBusinessDomain> getIndividualStocks(String code) {
        return stockService.getIndividualStocks(code);
    }

    /**
     * 个股周K线
     *
     * @param code
     * @return
     */
    @GetMapping("/stock/screen/weekkline")
    public R<List<Map>> getIndividualStocksByWeek(String code) {
        return stockService.getIndividualStocksByWeek(code);
    }

    /**
     * 获取个股最新分时行情数据
     *
     * @param code
     * @return
     */
    @GetMapping("/stock/screen/second/detail")
    public R<Map> getQuotesByHour(String code) {
        return stockService.getQuotesByHour(code);
    }

    /**
     * 个股最新交易流水行情
     * @param code
     * @return
     */
    @GetMapping("/stock/screen/second")
    public R<List<Map>> getStockByNew(String code) {
        return stockService.getStockByNew(code);
    }



}