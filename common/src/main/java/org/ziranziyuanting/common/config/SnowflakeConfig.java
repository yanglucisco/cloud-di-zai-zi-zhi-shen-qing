package org.ziranziyuanting.common.config;

import lombok.Data;

import java.util.Date;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Configuration
@ConfigurationProperties(prefix = "snowflake")
@Validated // 开启配置校验
@Data 
public class SnowflakeConfig {
    private long workerId;
    private long datacenterId;
    private Date startTimestamp;
    // @Bean
    // Snowflake snowflake() {
    //     // 使用自定义配置
    //     return new Snowflake(startTimestamp, workerId, datacenterId, true);
    // }
    @Bean
    CommonTest commonTest(){
        return new CommonTest();
    }
}
