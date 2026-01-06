package org.ziranziyuanting.rolemanage.param;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SysRoleShouQuanCaiDan {
    @NotNull(message = "菜单ID不能为空")
    private Long id;
    private List<Long> btnIds;
}
