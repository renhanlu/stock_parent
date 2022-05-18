package com.itheima.stock.vo.req;

import lombok.Data;

import java.util.List;

/**
 * @Author Renhanlu
 * @Date 2022/5/18 18:19
 * @Version 1.0
 */
@Data
public class UpdateMsgReqVo {
    private String userId;

    private List roleIds;
}
