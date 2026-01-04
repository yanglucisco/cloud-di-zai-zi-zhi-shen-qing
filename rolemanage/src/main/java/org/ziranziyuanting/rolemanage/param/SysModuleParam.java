package org.ziranziyuanting.rolemanage.param;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
public class SysModuleParam {
    /*
    {
  "sortCode": 99,
  "title": "测试模块2",
  "icon": "down-circle-outlined",
  "color": "#029688"
} */
    @NotNull(message = "sortCode不能为空")
    private Integer sortCode;
    @NotBlank(message = "title不能为空")
    private String title;
    @NotBlank(message = "icon不能为空")
    private String icon;
    @NotBlank(message = "color不能为空")
    private String color;
}
