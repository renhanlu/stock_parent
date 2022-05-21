package com.itheima.stock.sharding;

import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.Collection;
import java.util.Date;

/**
 * @author by itheima
 * @Date 2022/3/27
 * @Description 为stock_rt_info表定义精准匹配类
 */
public class StockRtInfoPreciseShardingAlgorithm4Table implements PreciseShardingAlgorithm<Date> {

    /**
     *
     * @param tbNames 分表名称集合
     * @param shardingValue 分片键数据的封装
     * @return 返回具体某一张物理表
     */
    @Override
    public String doSharding(Collection<String> tbNames, PreciseShardingValue<Date> shardingValue) {
        //获取日期对象
        Date date = shardingValue.getValue();
        //获取年月组装的字符串，比如：202203
        String sufixDate = new DateTime(date).toString(DateTimeFormat.forPattern("yyyyMM"));
        //从tbNames集合中查找以sufixDate结尾的数据
        String tableName = tbNames.stream().filter(tbName -> tbName.endsWith(sufixDate)).findFirst().get();
        return tableName;
    }
}