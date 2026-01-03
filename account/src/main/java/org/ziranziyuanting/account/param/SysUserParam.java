package org.ziranziyuanting.account.param;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SysUserParam {
    /*
    {
  "gender": "男",
  "account": "testyl",
  "name": "测试杨璐",
  "orgId": "1543842934270394368",
  "positionId": "1543899639134019591"
} */
    @NotBlank(message = "性别不能为空")
    private String gender;  
    @NotBlank(message = "账号不能为空")
    private String account;  
    @NotBlank(message = "姓名不能为空")
    private String name;
    @NotNull(message = "组织不能为空")
    private Long orgId;  
    @NotNull(message = "岗位不能为空")
    private Long positionId;
    private Long directorId;
}
