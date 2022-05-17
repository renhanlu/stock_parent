package com.itheima.stock.service.impl;

import com.google.common.collect.Lists;
import com.itheima.stock.common.domain.StockInfoConfig;
import com.itheima.stock.mapper.*;
import com.itheima.stock.pojo.StockBlockRtInfo;
import com.itheima.stock.pojo.StockMarketIndexInfo;
import com.itheima.stock.pojo.StockRtInfo;
import com.itheima.stock.service.StockTimerTaskService;
import com.itheima.stock.utils.DateTimeUtil;
import com.itheima.stock.utils.IdWorker;
import com.itheima.stock.utils.ParseType;
import com.itheima.stock.utils.ParserStockInfoUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Slf4j
public class StockTimerTaskServiceImpl implements StockTimerTaskService {
    @Autowired
    private StockInfoConfig stockInfoConfig;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private StockMarketIndexInfoMapper stockMarketIndexInfoMapper;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private StockBusinessMapper stockBusinessMapper;
    @Autowired
    private ParserStockInfoUtil parserStockInfoUtil;
    @Autowired
    private StockRtInfoMapper stockRtInfoMapper;
    @Autowired
    private StockBlockRtInfoMapper stockBlockRtInfoMapper;
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Autowired
    private StockOuterMarketIndexInfoMapper stockOuterMarketIndexInfoMapper;


    /**
     * 大盘数据实时采集
     */
    @Override
    public void getInnerMarketInfo() {
        String url = stockInfoConfig.getMarketUrl() + String.join(",", stockInfoConfig.getInner());
//        构建请求地址，添加请求头
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Referer", "https://finance.sina.com.cn/stock/");
        httpHeaders.add("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36");
        HttpEntity<Object> entity = new HttpEntity<>(httpHeaders);
/**           参数1 url路径
 *            参数2 请求头
 *            参数3 响应的数据类型
 *         post请求
 */
        String s = restTemplate.postForObject(url, entity, String.class);
//        正则表达式
        String reg = "var hq_str_(.+)=\"(.+)\";";
        Pattern pattern = Pattern.compile(reg);
        //匹配字符串
        Matcher matcher = pattern.matcher(s);
        List<StockMarketIndexInfo> list = new ArrayList<>();
        while (matcher.find()) {
//            获取大盘code
            String stockCode = matcher.group(1);
//            获取大盘信息
            String stockMsg = matcher.group(2);
            String[] msg = stockMsg.split(",");
            String name = msg[0];
            BigDecimal openPoint = new BigDecimal(msg[1]);
//            昨日开盘价
            BigDecimal preClosePoint = new BigDecimal(msg[2]);
            // 当前点
            BigDecimal curPoint = new BigDecimal(msg[3]);
            //获取大盘最高点
            BigDecimal maxPoint = new BigDecimal(msg[4]);
            //获取大盘的最小点
            BigDecimal minPoint = new BigDecimal(msg[5]);
            //获取成交量
            Long tradeAmt = Long.valueOf(msg[8]);
            //获取成交金额
            BigDecimal tradeVol = new BigDecimal(msg[9]);
            //时间
            Date curTime = DateTimeUtil.getDateTimeWithoutSecond(msg[30] + " " + msg[31]).toDate();
            StockMarketIndexInfo stockMarketIndexInfo = StockMarketIndexInfo.builder().id(idWorker.nextId())
                    .marketCode(stockCode).marketName(name).openPoint(openPoint)
                    .curPoint(curPoint).preClosePoint(preClosePoint)
                    .tradeAmount(tradeAmt).tradeVolume(tradeVol).maxPoint(maxPoint)
                    .minPoint(minPoint).curTime(curTime).build();
//            组装到一个集合中
            list.add(stockMarketIndexInfo);
        }

        if (CollectionUtils.isEmpty(list)) {
            log.info("长度{}", list.size());
            return;
        }
//        批量插入到数据库中
        int count = stockMarketIndexInfoMapper.insertStock(list);
        log.info("插入数量{}", count);


    }

    /**
     * 股票数据实时采集
     */

    @Override
    public void getStockCnInfo() {
        List<String> stockIds = stockBusinessMapper.getStockIds();
        stockIds=stockIds.stream().map(s -> {
            return s.startsWith("6") ? "sh" + s : "sz" + s;
        }).collect(Collectors.toList());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Referer", "https://finance.sina.com.cn/stock/");
        httpHeaders.add("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36");
        HttpEntity<Object> HttpEntity = new HttpEntity<>(httpHeaders);
        Lists.partition(stockIds, 15).stream().forEach(list -> {
            String url = stockInfoConfig.getMarketUrl() + String.join(",", list);
            String result = restTemplate.postForObject(url, HttpEntity, String.class);
            List<StockRtInfo> infos = parserStockInfoUtil.parser4StockOrMarketInfo(result, ParseType.ASHARE);
            log.info("当前采集的股票信息记录数：{}", infos.size());
            //            批量添加
            int count = stockRtInfoMapper.insertStock(infos);
            log.info("插入数量{}", count);
        });
    }

    /**
     * 国内盘块数据拉取
     */
    @Override
    public void getPlateStock() {
        String forObject = restTemplate.getForObject(stockInfoConfig.getBlockUrl(), String.class);
        List<StockBlockRtInfo> stockBlockRtInfos = parserStockInfoUtil.parse4StockBlock(forObject);
        log.info("数据为{}",stockBlockRtInfos.size());
        threadPoolTaskExecutor.execute(()->{
            Lists.partition(stockBlockRtInfos,20).forEach(list->{
                //20个一组，批量插入
                //        批量添加
                int count = stockBlockRtInfoMapper.insertStockAll(stockBlockRtInfos);
                log.info("插入数量{}",count);
            });
        });


//        String rag = "var S_Finance_bankuai_sinaindustry = (.*)";
//        Pattern compile = Pattern.compile(rag);
//        Matcher matcher = compile.matcher(forObject);
//        ArrayList<Object> list = new ArrayList<>();
//        while (matcher.find()) {
//            String jsonStr = forObject.substring(forObject.indexOf("=") + 1);
//            HashMap mapInfo = new Gson().fromJson(jsonStr, HashMap.class);
//
//            String s = mapInfo.toString();
//            String[] stock = s.split(",");
//            板块编码
//            String label = stock[0];
//            //板块行业
//            String blockName = stock[1];
//            //板块公司数量
//            Integer companyNum = Integer.valueOf(stock[2]);
//            //均价
//            BigDecimal avgPrice = new BigDecimal(stock[3]);
//            //涨跌幅度
//            BigDecimal priceLimit = new BigDecimal(stock[5]);
//            //总成交量
//            Long tradeAmount = Long.valueOf(stock[6]);
//            //总成交金额
//            BigDecimal tradeVolume = new BigDecimal(stock[7]);
//            //当前日期
//            Date now = DateTimeUtil.getDateTimeWithoutSecond(DateTime.now()).toDate();
//            StockBlockRtInfo build = StockBlockRtInfo.builder().id(idWorker.nextId() + "").label(label)
//                    .blockName(blockName).companyNum(companyNum).avgPrice(avgPrice).curTime(now)
//                    .updownRate(priceLimit).tradeAmount(tradeAmount).tradeVolume(tradeVolume)
//                    .build();
//            list.add(build);
//        }
//        System.out.println(list);
    }

    /**
     * 国外板块数据拉取
     */
    @Override
    public void getOutStock() {
        String url = stockInfoConfig.getOutUrl() + String.join(",", stockInfoConfig.getOuter());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Referer", "https://finance.sina.com.cn/stock/");
        httpHeaders.add("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/99.0.4844.82 Safari/537.36");
        HttpEntity<Object> HttpEntity = new HttpEntity<>(httpHeaders);
        String s = restTemplate.postForObject(url, HttpEntity, String.class);
        List list = parserStockInfoUtil.parser4StockOrMarketInfo(s, ParseType.OUTER);
        //  var hq_str_b_FSSTI="富时新加坡海峡时报指数,3123.68,-2.96,-0.09";
        //			大盘code      大盘名称       大盘点数    涨跌值    涨幅
//        批量插入到数据库中

        int count =stockOuterMarketIndexInfoMapper.insertAll(list);
        log.info("当前数量",list.size());
        log.info("插入数量{}", count);
    }


}