package org.ziranziyuanting.account.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysOrgVO {
    private String id;       // Changed to String
    private String parentId; // Changed to String
    private String name;
    private String code;
    private Integer sortCode;
    private String category;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    
    // For tree structure if needed, otherwise can be removed for simple list
    private List<SysOrgVO> children; 
}
