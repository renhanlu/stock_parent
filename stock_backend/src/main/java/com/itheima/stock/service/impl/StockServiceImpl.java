package com.itheima.stock.service.impl;

import com.itheima.stock.common.domain.InnerMarketDomain;
import com.itheima.stock.common.domain.StockBlockDomain;
import com.itheima.stock.common.domain.StockInfoConfig;
import com.itheima.stock.mapper.StockBlockRtInfoMapper;
import com.itheima.stock.mapper.StockBusinessMapper;
import com.itheima.stock.mapper.StockMarketIndexInfoMapper;
import com.itheima.stock.service.StockService;
import com.itheima.stock.utils.DateTimeUtil;
import com.itheima.stock.vo.R;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service()
public class StockServiceImpl implements StockService {

    @Autowired
    private StockBusinessMapper stockBusinessMapper;

    @Autowired
    private StockInfoConfig stockInfoConfig;

    @Autowired
    private StockMarketIndexInfoMapper stockMarketIndexInfoMapper;

    @Autowired
    protected StockBlockRtInfoMapper stockBlockRtInfoMapper;


    @Override
    public R<List<InnerMarketDomain>> getMarketAll() {
//        1.获取国内大盘数据
        List<String> inner = stockInfoConfig.getInner();
//        2.获取最近股票交易日期
        Date date = DateTimeUtil.getLastDate4Stock(DateTime.now()).toDate();
//         TODO 伪数据 后期删除
        date=DateTime.parse("2022-01-02 09:32:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
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
} 