package com.itheima.stock.vo.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author Renhanlu
 * @Date 2022/5/22 10:14
 * @Version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRoleReqVo {

    private String id;

    private String name;


    private String description;

    private List<String> permissionsIds;
}
