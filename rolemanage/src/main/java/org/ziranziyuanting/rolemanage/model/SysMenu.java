package org.ziranziyuanting.rolemanage.model;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SysMenu {
    private String icon;
    private String title;
    private String name;
    private String type;
    private String path;
    private String component;
    private Long id;
    private Long pid;
    @Builder.Default
    private List<SysMenu> children = new ArrayList<>();
    public void addSysMenu(SysMenu sysMenu){
        children.add(sysMenu);
    }
}
