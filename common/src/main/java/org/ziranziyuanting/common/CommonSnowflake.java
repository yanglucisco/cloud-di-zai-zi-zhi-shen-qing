package org.ziranziyuanting.common;

import java.util.Date;

import cn.hutool.core.lang.Snowflake;

public class CommonSnowflake {
    private Snowflake snowflake;
    public CommonSnowflake(Date epochDate, long workerId, long dataCenterId, boolean isUseSystemClock) {
        this.snowflake = new Snowflake(epochDate, workerId, dataCenterId, isUseSystemClock);
    }
    public Long nextId() {
        return snowflake.nextId();
    }
}
