package org.ziranziyuanting.rolemanage.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SysMenu {
    /**
     * icon: HomeOutlined, text: '首页', key: '2', type: 'subMenu', path: '/login', children:
     */
    private String icon;
    private String text;
    private String key;
    private String type;
    private String path;
    @Builder.Default
    private List<SysMenu> children = new ArrayList<>();
    public void addSysMenu(SysMenu sysMenu){
        children.add(sysMenu);
    }
}
