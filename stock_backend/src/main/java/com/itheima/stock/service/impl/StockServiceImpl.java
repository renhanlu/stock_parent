package com.itheima.stock.service.impl;

import com.alibaba.excel.EasyExcel;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.stock.common.domain.*;
import com.itheima.stock.mapper.*;
import com.itheima.stock.service.StockService;
import com.itheima.stock.utils.DateTimeUtil;
import com.itheima.stock.vo.req.PageResult;
import com.itheima.stock.vo.resp.R;
import lombok.extern.java.Log;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Log
public class StockServiceImpl implements StockService {



    @Autowired
    private StockInfoConfig stockInfoConfig;

    @Autowired
    private StockMarketIndexInfoMapper stockMarketIndexInfoMapper;

    @Autowired
    private StockBlockRtInfoMapper stockBlockRtInfoMapper;

    @Autowired
    private StockRtInfoMapper stockRtInfoMapper;

    @Autowired
    private StockOuterMarketIndexInfoMapper stockOuterMarketIndexInfoMapper;

    @Override
    public R<List<InnerMarketDomain>> getMarketAll() {
//        1.获取国内大盘数据
        List<String> inner = stockInfoConfig.getInner();
//        2.获取最近股票交易日期
        Date date = DateTimeUtil.getLastDate4Stock(DateTime.now()).toDate();
//         TODO 伪数据 后期删除
        date = DateTime.parse("2022-01-02 09:32:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
//        3. 调用dao层
        List<InnerMarketDomain> allByMarket = stockMarketIndexInfoMapper.getAllByMarket(inner, date);
        return R.ok(allByMarket);
    }

    @Override
    public R<List<StockBlockDomain>> getInlandMarket() {
//        1.获取最近交易的时间
        Date date = DateTimeUtil.getLastDate4Stock(DateTime.now()).toDate();
        //         TODO 伪数据 后期删除
        date = DateTime.parse("2022-01-14 16:57:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
//        2. 调用dao层 并返回
        List<StockBlockDomain> allMarketBytime = stockBlockRtInfoMapper.getAllMarketBytime(date);
        return R.ok(allMarketBytime);
    }

    /*
     * 查询最新交易信息
     * */
    @Override
    public R<List<StockUpdownDomain>> getLatest() {
        Date date = DateTimeUtil.getLastDate4Stock(DateTime.now()).toDate();
        // TODO: 2022/5/10 伪数据后期删除
        date = DateTime.parse("2021-12-30 09:42:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        List<StockUpdownDomain> allTransationData = stockRtInfoMapper.getAllTransationData(date);
        return R.ok(allTransationData);
    }


    @Override
//PageInfo<StockUpdownDomain>
    public R<PageResult<StockUpdownDomain>> getbyPage(Integer page, Integer pageSize) {
        PageHelper.startPage(page, pageSize);
        List<StockUpdownDomain> allByLimit = stockRtInfoMapper.getAllByLimt();
        PageInfo<StockUpdownDomain> PageInfo = new PageInfo<>(allByLimit);
        PageResult<StockUpdownDomain> pageResult = new PageResult<>(PageInfo);
        return R.ok(pageResult);
    }

    @Override
    public R<Map> getStockCount() {
        // TODO: 2022/5/11  假数据后期删除
        Date opentime = DateTimeUtil.getOpenDate(DateTime.now()).toDate();
        opentime  = DateTime.parse("2022-01-06 09:25:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        Date closetime = DateTimeUtil.getCloseDate(DateTime.now()).toDate();
        closetime=DateTime.parse("2022-01-06 14:25:00",DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        List<Map> upDown = stockRtInfoMapper.getStockCount(opentime, closetime, 1);
        List<Map> onDown = stockRtInfoMapper.getStockCount(opentime, closetime, 0);
        HashMap<Object, Object>  map = new HashMap<>();
        map.put("count", upDown);
        map.put("time", onDown);
        return R.ok(map);
    }

    /**
     *
     * @param response
     * @param page
     * @param pageSize
     */
    @Override
    public void stockExport(HttpServletResponse response, Integer page, Integer pageSize) {
        try {
            //设置页码
            PageHelper.startPage(page, pageSize);
            //1.设置响应数据的类型:excel
            response.setContentType("application/vnd.ms-excel");
            //2.设置响应数据的编码格式
            response.setCharacterEncoding("utf-8");
            //3.设置默认的文件名称
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode("stockRt", "UTF-8");
            //设置默认文件名称
            response.setHeader("content-disposition", "attachment;filename=" + fileName + ".xlsx");
            List<StockUpdownDomain> all = stockRtInfoMapper.getAllByLimt();
            // 写入数据
            EasyExcel.write(response.getOutputStream(),StockUpdownDomain.class).sheet("股票信息").doWrite(all);
        } catch (IOException e) {
           log.info("股票导出excel失败");
        }
    }

    /**
     * 外盘数据去先4
     * @return
     */
    @Override
    public R<List<StockOutDomain>> getOutStock() {
        List<String> inner = stockInfoConfig.getOuter();
        Date date = DateTimeUtil.getLastDate4Stock(DateTime.now()).toDate();
        date = DateTime.parse("2022-01-01 10:57:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        List<StockOutDomain> outStock = stockOuterMarketIndexInfoMapper.getOutStock(date, inner);
        return R.ok(outStock);
    }


} 