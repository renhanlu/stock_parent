package com.itheima.stock.sharding;

import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;

import java.util.Collection;
import java.util.Date;

/**
 * 要求：库内表没有水平分表处理，且逻辑表与物理表必须一致
 * @author by itheima
 * @Date 2022/3/27
 * @Description 定义公共精准匹表的类
 * 因为 股票流水 板块流水 大盘流水 分库的策略一致，所以它们可以公用该类
 */
public class CommonTbPreciseShardingAlgorithm implements PreciseShardingAlgorithm<Date> {

    /**
     * @param tbNames 物理表名称集合
     * @param shardingValue 分偏键相关信息的封装
     * @return 具体的数据源名称
     */
    @Override
    public String doSharding(Collection<String> tbNames, PreciseShardingValue<Date> shardingValue) {
        //因为对于板块表 和 大盘流水表 仅仅做了分库处理，没有做分表处理，也就以为这逻辑表与物理表一致
        //获取逻辑表
        String logicTableName = shardingValue.getLogicTableName();
        // String logicTableName = tbNames.stream().findFirst().get();
        return logicTableName;
    }
}