package org.ziranziyuanting.rolemanage.param;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SysButtonParam {
    @NotNull(message = "sortCode不能为空")
    private Integer sortCode;
    @NotNull(message = "parentId不能为空")
    private Long parentId;
    @NotBlank(message = "name不能为空")
    private String name;
    @NotBlank(message = "编码不能为空")
    private String code; 
}
