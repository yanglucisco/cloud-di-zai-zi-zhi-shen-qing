package org.ziranziyuanting.rolemanage.param;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SysRoleParam {
    /*
    {
  "sortCode": 99,
  "name": "测试角色",
  "category": "GLOBAL"
} */
    @NotNull(message = "排序码不能为空")
    private Integer sortCode;
    @NotBlank(message = "名称不能为空")
    private String name;
    @NotBlank(message = "分类不能为空")
    private String category;
}
