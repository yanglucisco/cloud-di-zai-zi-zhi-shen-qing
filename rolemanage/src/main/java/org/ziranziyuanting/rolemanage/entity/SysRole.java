package org.ziranziyuanting.rolemanage.entity;

import org.ziranziyuanting.common.entity.CommonEntity;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Builder
public class SysRole extends CommonEntity {
    /** 组织id */
    private String orgId;

    /** 名称 */
    private String name;

    /** 编码 */
    private String code;

    /** 分类 */
    private String category;

    /** 排序码 */
    private Integer sortCode;

    /** 扩展信息 */
    private String extJson;
}

