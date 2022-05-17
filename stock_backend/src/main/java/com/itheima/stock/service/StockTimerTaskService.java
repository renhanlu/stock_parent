package com.itheima.stock.service;

/**
 *
 * @author Renhanlu
 */
public interface StockTimerTaskService {

    /**
     * 获取国内大盘的实时数据信息
     */
    void getInnerMarketInfo();

    /**
     * 获取国内股票数据
     */
    void getStockCnInfo();

    /**
     * 板块数据拉取
     */
    void getPlateStock();

    /**
     * 国外板块数据拉取
     */
    void getOutStock();


}
