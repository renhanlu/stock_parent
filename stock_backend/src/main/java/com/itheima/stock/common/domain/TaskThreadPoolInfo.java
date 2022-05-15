package com.itheima.stock.common.domain;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Renhanlu
 */
@ConfigurationProperties(prefix = "task.pool")
@Data
public class TaskThreadPoolInfo {

    private Integer corePoolSize;

    private Integer maxPoolSize;

    private Integer keepAliveSeconds;

    private Integer queueCapacity;
}
