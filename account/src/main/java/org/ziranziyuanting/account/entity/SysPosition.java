package org.ziranziyuanting.account.entity;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Builder
public class SysPosition extends CommonEntity {

    /** 组织id */
    private Long orgId;

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
