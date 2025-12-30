package org.ziranziyuanting.common;

import org.springframework.stereotype.Component;
import org.ziranziyuanting.common.config.SnowflakeConfig;

import cn.hutool.core.lang.Snowflake;

@Component
public class CommonSnowflake {
    // private cn.hutool.core.lang.Snowflake snowflake;
    private Snowflake snowflake;
    public CommonSnowflake(SnowflakeConfig snowflakeConfig) {
        this.snowflake = new Snowflake(snowflakeConfig.getStartTimestamp(), snowflakeConfig.getWorkerId(), snowflakeConfig.getDatacenterId(), true);
        // this.snowflake = snowflake;
    }
    public Long nextId() {
        return snowflake.nextId();
    }
}
