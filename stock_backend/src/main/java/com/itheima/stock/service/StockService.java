package com.itheima.stock.service;

import com.itheima.stock.common.domain.InnerMarketDomain;
import com.itheima.stock.common.domain.StockBlockDomain;
import com.itheima.stock.common.domain.StockOutDomain;
import com.itheima.stock.common.domain.StockUpdownDomain;
import com.itheima.stock.vo.req.PageResult;
import com.itheima.stock.vo.resp.R;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface StockService {

    /*
       查询国内大盘指数
     * */
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

    R<List<StockOutDomain>> getOutStock();

}

