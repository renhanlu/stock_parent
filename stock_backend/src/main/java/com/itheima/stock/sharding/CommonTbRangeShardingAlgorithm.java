package com.itheima.stock.sharding;

import org.apache.shardingsphere.api.sharding.standard.RangeShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingValue;

import java.util.Collection;
import java.util.Date;

/**
 * @author by itheima
 * @Date 2022/3/27
 * @Description 定义公共表范围查询的策略类
 */
public class CommonTbRangeShardingAlgorithm implements RangeShardingAlgorithm<Date> {
    /**
     *  select * from stock_rt_inf where cur_date between d1  and d2;
     * @param tbNames 逻辑表的名称集合
     * @param shardingValue 范围查询片键值相关信息的封装
     * @return
     */
    @Override
    public Collection<String> doSharding(Collection<String> tbNames, RangeShardingValue<Date> shardingValue) {
        return tbNames;
    }
}