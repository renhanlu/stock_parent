package com.itheima.stock.controller;

import com.itheima.stock.common.domain.InnerMarketDomain;
import com.itheima.stock.common.domain.StockBlockDomain;
import com.itheima.stock.service.StockService;
import com.itheima.stock.vo.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/quot")
@CrossOrigin
public class StockController {

    @Autowired
    private StockService stockService;

//    获取国内数据
    @GetMapping("/index/all")
    public R<List<InnerMarketDomain>> getMarketAll(){
        return stockService.getMarketAll();
    }
//  获取国内板块数据
    @GetMapping("/sector/all")
    public  R<List<StockBlockDomain>>  getAllinLand(){
        return stockService.getInlandMarket();
    }

}  