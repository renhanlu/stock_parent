package com.itheima.stock.service;

import com.itheima.stock.common.domain.InnerMarketDomain;
import com.itheima.stock.common.domain.StockBlockDomain;
import com.itheima.stock.vo.R;

import java.util.List;

public interface StockService {

    /*
       查询国内大盘指数
     * */
    R<List<InnerMarketDomain>> getMarketAll();

    /*
     查询国内板块指数
    * */
    R<List<StockBlockDomain>> getInlandMarket();

}

