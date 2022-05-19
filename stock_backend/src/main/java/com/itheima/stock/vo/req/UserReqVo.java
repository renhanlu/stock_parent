package com.itheima.stock.vo.req;

import lombok.Data;

/**
 * @Author Renhanlu
 * @Date 2022/5/18 10:44
 * @Version 1.0
 */
@Data
public class UserReqVo {

    private Integer pageNum;

    private Integer pageSize;


    private String username;

    private String nickname;

    private String startTime;

    private String endTime;



}
