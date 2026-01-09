package service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.ziranziyuanting.rolemanage.model.SysMenu;
import org.ziranziyuanting.rolemanage.service.impl.SysRoleServiceImpl;

public class SysRoleServiceImplTest {
    @Test
    public void treeTest(){
        SysRoleServiceImpl serviceImpl = new SysRoleServiceImpl(null, null);
        SysMenu sysMenu1 = SysMenu.builder().id(1l).pid(0l).build();
        SysMenu sysMenu2 = SysMenu.builder().id(2l).pid(0l).build();
        SysMenu sysMenu3 = SysMenu.builder().id(3l).pid(0l).build();
        SysMenu sysMenu4 = SysMenu.builder().id(11l).pid(1l).build();
        SysMenu sysMenu5 = SysMenu.builder().id(12l).pid(1l).build();
        SysMenu sysMenu6 = SysMenu.builder().id(21l).pid(2l).build();
        SysMenu sysMenu7 = SysMenu.builder().id(211l).pid(21l).build();
        SysMenu sysMenu8 = SysMenu.builder().id(2111l).pid(211l).build();
        List<SysMenu> list = List.of(sysMenu1, sysMenu8, sysMenu4, sysMenu3, sysMenu2, sysMenu6, sysMenu7, sysMenu5);
        List<SysMenu> tree = new ArrayList<>();
        serviceImpl.tree(list, tree);
        assertEquals(3, tree.size(), "tree的大小应该是3");
        assertEquals(2, sysMenu1.getChildren().size());
        assertEquals(1, sysMenu2.getChildren().size());
        assertEquals(0, sysMenu3.getChildren().size());
        assertEquals(0, sysMenu4.getChildren().size());
        assertEquals(0, sysMenu5.getChildren().size());
        assertEquals(1, sysMenu6.getChildren().size());
        assertEquals(1, sysMenu7.getChildren().size());
        assertEquals(0, sysMenu8.getChildren().size());
    }
}
