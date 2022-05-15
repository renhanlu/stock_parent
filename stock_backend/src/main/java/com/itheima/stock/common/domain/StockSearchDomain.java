package com.itheima.stock.common.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockSearchDomain {

    /**
     * 股票编码
     */
    private String code;


    /**
     * 股票名称
     */
    private String name;
}
