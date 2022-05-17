package com.itheima.stock.task;

import com.itheima.stock.service.StockTimerTaskService;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class StockJob {

    @Autowired
    private StockTimerTaskService stockTimerTaskService;

    /**
     * 国内板块数据定时拉取
     */
    @XxlJob("getPlateStock")
    public void getPlateStock() {
        stockTimerTaskService.getPlateStock();
    }

    /**
     * 定时获取国内大盘的实时数据信息
     */
    @XxlJob("getInnerMarketInfo")
    public void getInnerMarketInfo() {
        stockTimerTaskService.getInnerMarketInfo();
    }

    /**
     * 定时获取国内股票数据
     */
    @XxlJob("getStockCnInfo")
    public void getStockCnInfo() {
         stockTimerTaskService.getStockCnInfo();
    }

//    定时获取国外大盘数据
    @XxlJob("getOutStock")
    public  void getOutStock(){
        stockTimerTaskService.getOutStock();
    }
}