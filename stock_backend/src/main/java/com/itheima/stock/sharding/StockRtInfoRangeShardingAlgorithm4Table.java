package com.itheima.stock.sharding;

import com.google.common.collect.Range;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingValue;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * @author by itheima
 * @Date 2022/3/27
 * @Description 定义股票流水范围查询的算法类
 */
public class StockRtInfoRangeShardingAlgorithm4Table implements RangeShardingAlgorithm<Date> {

    /**
     *select * from stock_rt_inf where cur_date between d1  and d2;
     * @param tbNames 表名称集合
     * @param shardingValue 封装分偏键范围查询的对象
     *                      核心做法：就是将日期比较转化成数字比较
     * @return
     */
    @Override
    public Collection<String> doSharding(Collection<String> tbNames, RangeShardingValue<Date> shardingValue) {
        //获取范围对象
        Range<Date> valueRange = shardingValue.getValueRange();
        //判断是否有下限值
        if (valueRange.hasLowerBound()) {
            //获取下限值
            Date lowerDate = valueRange.lowerEndpoint();
            //获取年月组合的字符串，eg:202203
            String dateStr = new DateTime(lowerDate).toString(DateTimeFormat.forPattern("yyyyMM"));
            Integer intDate = Integer.valueOf(dateStr);
            //从tbNames集合中获取大于等于intDate的表的名称集合
            tbNames=tbNames.stream()
                    .filter(tbName->Integer.valueOf(tbName.substring(tbName.lastIndexOf("_")+1))>=intDate)
                    .collect(Collectors.toList());
        }
        //判断是否有上限值
        if (valueRange.hasUpperBound()) {
            //获取上限日期对象
            Date highDate = valueRange.upperEndpoint();
            //获取年月组合的字符串，eg:202203
            String dateStr = new DateTime(highDate).toString(DateTimeFormat.forPattern("yyyyMM"));
            Integer intDate = Integer.valueOf(dateStr);
            //从tbNames集合中获取大于等于intDate的表的名称集合
            tbNames=tbNames.stream()
                    .filter(tbName->Integer.valueOf(tbName.substring(tbName.lastIndexOf("_")+1))<=intDate)
                    .collect(Collectors.toList());
        }

        return tbNames;
    }
}