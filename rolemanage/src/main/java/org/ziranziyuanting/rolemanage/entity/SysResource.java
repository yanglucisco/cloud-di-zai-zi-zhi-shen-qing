package org.ziranziyuanting.rolemanage.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper=false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysResource extends org.ziranziyuanting.common.entity.CommonEntity {
    private Long parentId;
    private String title;
    private String name;
    private String code;
    private String category;
    private String module;
    private String menuType;
    private String path;
    private String component;
    private String icon;
    private String color;
    private String visible;
    private Integer sortCode;
    private String extJson;
}