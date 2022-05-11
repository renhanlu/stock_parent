package com.itheima.stock.controller;

import com.itheima.stock.common.domain.InnerMarketDomain;
import com.itheima.stock.common.domain.StockBlockDomain;
import com.itheima.stock.common.domain.StockOutDomain;
import com.itheima.stock.common.domain.StockUpdownDomain;
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

    /*
     * 查询沪深两市个股行情 ，以时间顺序和涨幅分页查询
     * */
    @GetMapping("/stock/all")
    public R<PageResult<StockUpdownDomain>> getDealsByLimit(Integer page, Integer pageSize) {
        return stockService.getbyPage(page, pageSize);
    }

    /**
     * 统计沪深两市涨停跌停数量
     * @return
     */
    @GetMapping("/stock/updown/count")
    public R<Map>  getStockCount(){
        return stockService.getStockCount();
    }

    /**
     * 国内大盘信息导出
     * @param response
     * @param page
     * @param pageSize
     */
    @GetMapping("/stock/export")
    public  void setFile(HttpServletResponse response,Integer page,Integer pageSize){
        stockService.stockExport(response, page, pageSize);
    }

    /**
     * 查询国外大盘数据
     * @return
     */

    @GetMapping("/external/index")
    public  R<List<StockOutDomain>> getOutStock(){
        return stockService.getOutStock();
    }



}  