package com.itheima.stock.vo.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author Renhanlu
 * @Date 2022/5/18 18:19
 * @Version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMsgReqVo {
    private String userId;

    private List<String> roleIds;
}
