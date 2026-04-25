package org.ziranziyuanting.account.vo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SysOrgTreeNodeVO {
    @JsonIgnore
    private String id;
    @JsonIgnore
    private String parentId;
    private String title;
    private String key;
    private String label;
    private String value;
    private List<SysOrgTreeNodeVO> children;
}
