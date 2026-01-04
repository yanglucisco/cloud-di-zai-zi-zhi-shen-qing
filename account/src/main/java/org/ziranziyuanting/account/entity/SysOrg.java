package org.ziranziyuanting.account.entity;

import org.ziranziyuanting.common.entity.CommonEntity;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 组织实体
 *
 * @author xuyuxiang
 * @date 2022/4/21 16:13
 **/
@Data
@EqualsAndHashCode(callSuper=false)
public class SysOrg extends CommonEntity {

    /** 父id */
    private Long parentId;

    /** 主管id */
    private Long directorId;

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
    public static SysOrg of(int sortCode, Long parentId, String name, String category){
        SysOrg r = new SysOrg();
        r.setCategory(category);
        r.setSortCode(sortCode);
        r.setParentId(parentId);
        r.setName(name);
        return r;
    }
}

