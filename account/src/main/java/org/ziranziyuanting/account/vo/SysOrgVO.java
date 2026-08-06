package org.ziranziyuanting.account.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String key;
    // For tree structure if needed, otherwise can be removed for simple list
    private List<SysOrgVO> children; 
}
