package org.ziranziyuanting.account.param;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class PageParam {
    @Min(0)
    private int page = 0; // Page index, starting from 0
    
    @Min(1)
    private int pageSize = 10; // Page size
}
