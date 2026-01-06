package org.ziranziyuanting.rolemanage.param;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SysRoleShouQuanCaiDanParam {
    @NotNull(message = "角色ID不能为空")
    private Long roleId;
    @NotEmpty(message = "菜单不能为空")
    private List<SysRoleShouQuanCaiDan> sysRoleShouQuanCaiDans;
}
