package com.itheima.stock.common.domain;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @author by itheima
 * @Date 2021/12/30
 * @Description
 */
@ConfigurationProperties(prefix = "stock")
@Data
public class StockInfoConfig {
    //a股大盘ID集合
    private List<String> inner;
    //外盘ID集合
    private List<String> outer;

//    股票区间
    private List<String> upDownRange;


    private String marketUrl;

    private String blockUrl;

    private String outUrl;
}