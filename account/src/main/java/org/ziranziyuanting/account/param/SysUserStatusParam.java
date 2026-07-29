package org.ziranziyuanting.account.param;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SysUserStatusParam {
    @NotNull
    private Long id;

    @NotBlank
    private String status;
}
