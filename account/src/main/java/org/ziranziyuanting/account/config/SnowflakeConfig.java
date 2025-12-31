package org.ziranziyuanting.account.config;

import lombok.Data;
import java.util.Date;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;
import org.ziranziyuanting.common.CommonSnowflake;

@Configuration
@ConfigurationProperties(prefix = "snowflake")
@Validated // 开启配置校验
@Data 
public class SnowflakeConfig {
    private long workerId;
    private long datacenterId;
    private Date startTimestamp;
    @Bean
    CommonSnowflake commonSnowflake(){
        return new CommonSnowflake(startTimestamp, workerId, datacenterId, true);
    }
}
