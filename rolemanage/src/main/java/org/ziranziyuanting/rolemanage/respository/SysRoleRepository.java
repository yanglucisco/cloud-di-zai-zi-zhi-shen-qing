package org.ziranziyuanting.rolemanage.respository;


import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.ziranziyuanting.common.repository.CommonReactiveCrudRepository;
import org.ziranziyuanting.rolemanage.entity.SysRole;
import org.ziranziyuanting.rolemanage.model.SysMenu;

import reactor.core.publisher.Flux;

public interface SysRoleRepository extends CommonReactiveCrudRepository<SysRole>
{
    /*
    String icon, String text, String key, String type, String path
    select new org.ziranziyuanting.rolemanage.model.SysMenu(re.ICON, re.TITLE, re.NAME, re.MENU_TYPE, re.PATH) from sys_relation r inner JOIN
              sys_resource re ON
              r.TARGET_ID = re.ID
              where r.OBJECT_ID = :userId */
    // 一对一查询
    // @Query("SELECT new com.example.dto.UserDTO(u.id, u.name, d.name) " +
    //        "FROM User u JOIN Department d ON u.departmentId = d.id " +
    //        "WHERE u.status = :status")
    // private String icon;
    // private String text;
    // private String key;
    // private String type;
    // private String path;
    @Query("select re.ICON as icon, re.TITLE as text, re.name as 'key', re.MENU_TYPE as type, re.PATH as path, re.ID as id, re.PARENT_ID as pid" +
            " from sys_relation r inner JOIN " + //
                "              sys_resource re ON " + //
                "              r.TARGET_ID = re.ID " + //
                "              where r.OBJECT_ID = :userId")
    Flux<SysMenu> findResourceWithUserId(@Param("userId") Long userId);
}
