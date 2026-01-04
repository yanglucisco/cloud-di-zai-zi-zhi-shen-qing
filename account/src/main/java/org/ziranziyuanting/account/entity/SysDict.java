package org.ziranziyuanting.account.entity;

import org.ziranziyuanting.common.entity.CommonEntity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Builder
public class SysDict extends CommonEntity {

    @NotNull(message = "父id不能为空")
    /** 父id */
    private Long parentId;
    @NotBlank(message = "字典文字不能为空")
    /** 字典文字 */
    private String dictLabel;
    @NotBlank(message = "字典值不能为空")   
    /** 字典值 */
    private String dictValue;
    @NotBlank(message = "分类不能为空")
    /** 分类 */
    private String category;
    @NotNull(message = "排序码不能为空")
    /** 排序码 */
    private Integer sortCode;
    /** 扩展信息 */
    private String extJson;
}
