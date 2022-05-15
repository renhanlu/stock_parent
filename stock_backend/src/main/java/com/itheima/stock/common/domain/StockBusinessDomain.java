package com.itheima.stock.common.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Renhanlu
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockBusinessDomain {
    /**
     * 股票编码
     */
    private String code;

    /**
     * 行业
     */
    private String trade;

    /**
     * 主营业务
     */
    private String business;

    /**
     * 公司名称
     */
    private String name;
}
