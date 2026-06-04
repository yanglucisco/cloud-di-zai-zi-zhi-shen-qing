package org.ziranziyuanting.account.param;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SysUserParam {
    private String gender;  
    @NotBlank(message = "账号不能为空")
    private String account;  
    @NotBlank(message = "姓名不能为空")
    private String name;
    @NotNull(message = "组织不能为空")
    private Long orgId;  
    private Long positionId;
    private Long directorId;
    @NotBlank(message = "密码不能为空")
    private String password;
}
