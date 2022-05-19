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
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;

/**
 * @author Renhanlu
 */
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

    @Autowired
    private StockBusinessMapper stockBusinessMapper;

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
        DateTime dateTime = DateTimeUtil.getLastDate4Stock(DateTime.now()).minusMinutes(1);
        Date date = dateTime.toDate();
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
//        2. 调用dao层 并返回
        List<StockBlockDomain> allMarketBytime = stockBlockRtInfoMapper.getAllMarketBytime(date);
        return R.ok(allMarketBytime);
    }

    /**
     * 查询最新交易信息前10
     *
     * @return
     */
    @Override
    public R<List<StockUpdownDomain>> getLatest() {
        Date date = DateTimeUtil.getLastDate4Stock(DateTime.now()).minusMinutes(1).toDate();
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
        Date opentime = DateTimeUtil.getOpenDate(DateTime.now()).minusMinutes(1).toDate();
        Date closetime = DateTimeUtil.getCloseDate(DateTime.now()).minusMinutes(1).toDate();
        List<Map> upDown = stockRtInfoMapper.getStockCount(opentime, closetime, 1);
        List<Map> onDown = stockRtInfoMapper.getStockCount(opentime, closetime, 0);
        HashMap<Object, Object> map = new HashMap<>();
        map.put("upList", upDown);
        map.put("downList", onDown);
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
        //1.获取T日和T-1日的开始时间和结束时间
        //1.1 获取最近股票有效交易时间点--T日时间范围
        DateTime lastDateTime = DateTimeUtil.getLastDate4Stock(DateTime.now());
        DateTime openDateTime = DateTimeUtil.getOpenDate(lastDateTime);
        //转化成java中Date,这样jdbc默认识别
        Date startTime4T = openDateTime.toDate();
        Date endTime4T=lastDateTime.toDate();

        //1.2 获取T-1日的区间范围
        //获取lastDateTime的上一个股票有效交易日
        DateTime preLastDateTime = DateTimeUtil.getPreviousTradingDay(lastDateTime);
        DateTime preOpenDateTime = DateTimeUtil.getOpenDate(preLastDateTime);
        Date startTime = preOpenDateTime.toDate();
        Date endTime=preLastDateTime.toDate();
        List<String> markedIds = stockInfoConfig.getInner();
        //3.分别查询T日和T-1日的交易量数据，得到两个集合
        //3.1 查询T日大盘交易统计数据
        List<Map> data4T=stockMarketIndexInfoMapper.etStockCompared(startTime4T,endTime4T,markedIds);
        if (CollectionUtils.isEmpty(data4T)) {
            data4T=new ArrayList<>();
        }
        //3.2 查询T-1日大盘交易统计数据
        List<Map> data4PreT=stockMarketIndexInfoMapper.etStockCompared(startTime,endTime,markedIds);
        if (CollectionUtils.isEmpty(data4PreT)) {
            data4PreT=new ArrayList<>();
        }
        //4.组装响应数据
        HashMap<String, List> info = new HashMap<>();
        info.put("volList",data4T);
        info.put("yesVolList",data4PreT);
        //5.返回数据
        return R.ok(info);
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
        DateTime openDate = DateTimeUtil.getLastDate4Stock(DateTime.now()).minusMinutes(1);
        Date curTime = openDate.toDate();
//        2.获取当前指定时间的开盘时间
        DateTime openDateTime = DateTimeUtil.getOpenDate(openDate).minusMinutes(1);
        Date startTime = openDateTime.toDate();
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
        DateTime dateTime = DateTimeUtil.getLastDate4Stock(DateTime.now()).minusMinutes(1);
        Date date = dateTime.toDate();
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
        StockMap.put("infos", list);
        return R.ok(StockMap);
    }

    /**
     * 个股日K数据查询
     *
     * @param code
     * @return
     */
    @Override
    public R<List<Stock4EvrDayDomain>> getTimeDay(String code) {
//        1.获取最近的交易时间
        DateTime lastDate4Stock = DateTimeUtil.getLastDate4Stock(DateTime.now()).minusMinutes(1);
        Date date = lastDate4Stock.toDate();
        // TODO: 2022/5/18
        date=DateTime.parse("2021-12-30 09:32:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
//        2. 获取开始时间
        Date toDate = lastDate4Stock.minusDays(20).toDate();
        toDate=DateTime.parse("2021-01-07 14:30:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        List<Stock4EvrDayDomain> stockByDay = stockRtInfoMapper.getTimeDay(toDate, date, code);
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
        List<StockOutDomain> outStock = stockOuterMarketIndexInfoMapper.getOutStock(date, inner);
        return R.ok(outStock);
    }

    /**
     * 模糊搜索股票信息 只支持股票代码 不支持名称
     *
     * @param searchStr
     * @return
     */
    @Override
    public R<List<StockSearchDomain>> selectStockByLike(String searchStr) {
        List<StockSearchDomain> stockSearchDomains = stockBusinessMapper.selectStockByLike(searchStr);
        return R.ok(stockSearchDomains);
    }

    /**
     * 个股主营业务查询
     *
     * @param code
     * @return
     */
    @Override
    public R<StockBusinessDomain> getIndividualStocks(String code) {
        StockBusinessDomain individualStocks = stockBusinessMapper.getIndividualStocks(code);
        return R.ok(individualStocks);
    }

    /**
     * 个股周K线
     *
     * @param code
     * @return
     */
    @Override
    public R<List<Map>> getIndividualStocksByWeek(String code) {
        List<Map> IndividualByWeek = stockRtInfoMapper.getIndividualStocksByWeek(code);
        return R.ok(IndividualByWeek);
    }

    /**
     * 获取个股最新分时行情数据
     *
     * @param code
     * @return
     */
    @Override
    public R<Map> getQuotesByHour(String code) {
//        1.获取最新的交易时间
        DateTime dateTime = DateTimeUtil.getLastDate4Stock(DateTime.now()).minusMinutes(2);
        Date date = dateTime.toDate();
        Map stockHour = stockRtInfoMapper.getQuotesByHour(code, date);
        if (CollectionUtils.isEmpty(stockHour)) {
            return R.error("数据为空");
        }
        return R.ok(stockHour);
    }

    /**
     * 个股实时交易流水查询
     * @param code 股票编码
     * @return
     */
    @Override
    public R<List<Map>> getStockByNew(String code) {
       DateTime dateTime = DateTimeUtil.getLastDate4Stock(DateTime.now()).minusMinutes(2);
        Date date = dateTime.toDate();
        // TODO: 2022/5/16 伪数据测试用 
        date=DateTime.parse("2021-12-30 14:47:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        List<Map> stockByNew = stockRtInfoMapper.getStockByNew(date, code);
        return R.ok(stockByNew);
    }
}