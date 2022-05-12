package com.itheima.stock.service.impl;

import com.alibaba.excel.EasyExcel;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.stock.common.domain.*;
import com.itheima.stock.mapper.StockBlockRtInfoMapper;
import com.itheima.stock.mapper.StockMarketIndexInfoMapper;
import com.itheima.stock.mapper.StockOuterMarketIndexInfoMapper;
import com.itheima.stock.mapper.StockRtInfoMapper;
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
import java.util.*;

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

    /**
     * 查询国内大盘数据
     *
     * @return
     */
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

    /**
     * 查询国内板块指数
     *
     * @return
     */
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

    /**
     * 分页查询国内个股
     *
     * @param page     当前页
     * @param pageSize 每页大小
     * @return
     */
    @Override
//PageInfo<StockUpdownDomain>
    public R<PageResult<StockUpdownDomain>> getbyPage(Integer page, Integer pageSize) {
        PageHelper.startPage(page, pageSize);
        List<StockUpdownDomain> allByLimit = stockRtInfoMapper.getAllByLimt();
        PageInfo<StockUpdownDomain> PageInfo = new PageInfo<>(allByLimit);
        PageResult<StockUpdownDomain> pageResult = new PageResult<>(PageInfo);
        return R.ok(pageResult);
    }

    /**
     * 统计沪深两市T日(当前股票交易日)每分钟的涨跌停数据
     *
     * @return
     */
    @Override
    public R<Map> getStockCount() {
        // TODO: 2022/5/11  假数据后期删除
        Date opentime = DateTimeUtil.getOpenDate(DateTime.now()).toDate();
        opentime = DateTime.parse("2022-01-06 09:25:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        Date closetime = DateTimeUtil.getCloseDate(DateTime.now()).toDate();
        closetime = DateTime.parse("2022-01-06 14:25:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        List<Map> upDown = stockRtInfoMapper.getStockCount(opentime, closetime, 1);
        List<Map> onDown = stockRtInfoMapper.getStockCount(opentime, closetime, 0);
        HashMap<Object, Object> map = new HashMap<>();
        map.put("count", upDown);
        map.put("time", onDown);
        return R.ok(map);
    }

    /**
     * Excel数据导出
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
            EasyExcel.write(response.getOutputStream(), StockUpdownDomain.class).sheet("股票信息").doWrite(all);
        } catch (IOException e) {
            log.info("股票导出excel失败");
        }
    }


    /**
     * 统计国内A股大盘T日和T-1日成交量对比功能（成交量为沪市和深市成交量之和）
     *
     * @return
     */
    @Override
    public R<Map> getStockCompared() {
//        1. 获取当前时间
        DateTime tCStockTime = DateTimeUtil.getLastDate4Stock(DateTime.now());
//        1.1获取距离当前时间最近的开盘时间
        DateTime tOpenTime = DateTimeUtil.getOpenDate(tCStockTime);
        //转换成java date
        Date tcDate = tCStockTime.toDate();
        Date toTime = tOpenTime.toDate();
//        2.1 获取上一个交易时间
        DateTime pretTime = DateTimeUtil.getPreviousTradingDay(tCStockTime);
//        2.1 获取上一个交易时间的开盘时间
        DateTime openDate = DateTimeUtil.getOpenDate(pretTime);
        // 转换java date
        Date date = pretTime.toDate();
        Date toDate = openDate.toDate();
        // TODO: 2022/5/12 设置假数据
        tcDate = DateTime.parse("2022-01-03 09:30:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        toTime = DateTime.parse("2022-01-03 14:40:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        date = DateTime.parse("2022-01-02 09:30:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        toDate = DateTime.parse("2022-01-02 14:40:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        // \TODO: 2022/5/12 假数据
        List<String> inner = stockInfoConfig.getInner();
        List<Map> crTime = stockMarketIndexInfoMapper.etStockCompared(tcDate, toTime, inner);
        List<Map> yesTime = stockMarketIndexInfoMapper.etStockCompared(date, toDate, inner);
        Map maps = new HashMap<>();
        maps.put("volList", crTime);
        maps.put("yesVolList", yesTime);
        return R.ok(maps);
    }

    /**
     * 获取个股分时数据
     *
     * @param code
     * @return
     */
    @Override
    public R<List<Stock4MinuteDomain>> getStockHour(String code) {
//        1.获取最近的交易时间
        DateTime openDate = DateTimeUtil.getLastDate4Stock(DateTime.now());
        Date curTime = openDate.toDate();
        // TODO: 2022/5/12 伪数据
        curTime = DateTime.parse("2021-12-30 09:30:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
//        2.获取当前指定时间的开盘时间
        DateTime openDateTime = DateTimeUtil.getOpenDate(openDate);
        Date startTime = openDateTime.toDate();
        // TODO: 2022/5/12 伪数据
        startTime = DateTime.parse("2021-12-30 14:30:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        List<Stock4MinuteDomain> hourStock = stockRtInfoMapper.getStockHour(curTime, startTime, code);
        return R.ok(hourStock);
    }

    /**
     * 统计当前时间下（精确到分钟），股票在各个涨幅区间的数量
     *
     * @return
     */

    @Override
    public R<Map> getStockRateCount() {
//        1.获取最近的交易时间
        DateTime dateTime = DateTimeUtil.getLastDate4Stock(DateTime.now());
        Date date = dateTime.toDate();
        // TODO: 2022/5/12 假数据后期删除
        date = DateTime.parse("2022-01-06 09:55:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        // 获取数据
        List<Map> countUpDown = stockRtInfoMapper.getStockRateCount(date);
        ArrayList<Map> list = new ArrayList<>();
        for (String stock : stockInfoConfig.getUpDownRange()) {
            Map map = null;
            for (Map m : countUpDown) {
                if (m.get("title").equals(stock)) {
                    map = m;
                    break;
                }
            }
            if (map == null) {
                map = new HashMap();
                map.put("title", stock);
                map.put("count", 0);
            }
            list.add(map);
        }
        Map StockMap = new HashMap<>();
        String data = new DateTime(date).toString(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm"));
        StockMap.put("time", data);
        StockMap.put("infos",list);
        return R.ok(StockMap);
    }

    /**
     * 个股日K数据查询
     * @param code
     * @return
     */
    @Override
    public R<List<Stock4EvrDayDomain>> getTimeDay(String code) {
//        1.获取最近的交易时间
        DateTime lastDate4Stock = DateTimeUtil.getLastDate4Stock(DateTime.now());
        Date date = lastDate4Stock.toDate();
        // TODO: 2022/5/12 伪数据后期删除
        date= DateTime.parse("2022-01-07 15:00:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
//        2. 获取开始时间
        Date toDate = lastDate4Stock.minusDays(20).toDate();
        // TODO: 2022/5/12  伪数据 
        toDate=  DateTime.parse("2022-01-01 09:30:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
       List<Stock4EvrDayDomain> stockByDay= stockRtInfoMapper.getTimeDay(toDate, date, code);
        return R.ok(stockByDay);
    }

    /**
     * 获取外盘数据前四
     *
     * @return
     */
    @Override
    public R<List<StockOutDomain>> getOutStock() {
        //获取外盘大盘名称
        List<String> inner = stockInfoConfig.getOuter();
        Date date = DateTimeUtil.getLastDate4Stock(DateTime.now()).toDate();
        date = DateTime.parse("2022-01-01 10:57:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        List<StockOutDomain> outStock = stockOuterMarketIndexInfoMapper.getOutStock(date, inner);
        return R.ok(outStock);
    }
} 