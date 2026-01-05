package org.ziranziyuanting.rolemanage.param;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SysMenuParam {
  /*
  {
  "menuType": "MENU",
  "visible": "TRUE",
  "sortCode": 99,
  "title": "测试菜单",
  "parentId": 0,
  "module": "1548901111999770525",
  "path": "/testmenu",
  "component": "testmenu",
  "name": "testmenu",
  "icon": "up-circle-outlined"
} */
    @NotBlank(message = "menuType不能为空")
    private String menuType;
    @NotBlank(message = "visible不能为空")
    private String visible;
    @NotNull(message = "sortCode不能为空")
    private Integer sortCode;
    @NotBlank(message = "title不能为空")
    private String title;
    @NotNull(message = "parentId不能为空")
    private Long parentId;
    @NotBlank(message = "module不能为空")
    private String module;
    // @NotBlank(message = "path不能为空")
    private String path;
    // @NotBlank(message = "component不能为空")
    private String component;
    @NotBlank(message = "name不能为空")
    private String name;
    @NotBlank(message = "icon不能为空")
    private String icon;
}
