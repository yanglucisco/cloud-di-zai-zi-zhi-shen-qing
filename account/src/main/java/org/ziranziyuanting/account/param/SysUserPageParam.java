package org.ziranziyuanting.account.param;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class SysUserPageParam {
    @Min(0)
    private int page = 0;

    @Min(1)
    private int pageSize = 10;

    /** 关键词（模糊匹配账号或姓名） */
    private String keyword;

    /** 用户状态：ENABLE / DISABLE */
    private String status;

    /** 组织ID */
    private Long orgId;
}
