package com.itheima.stock.service;

import com.itheima.stock.pojo.StockBusiness;

import java.util.List;

public interface StockService {
    /**
     * 获取所有股票信息
     * @return
     */
    List<StockBusiness> getAllStockBusiness();
}   