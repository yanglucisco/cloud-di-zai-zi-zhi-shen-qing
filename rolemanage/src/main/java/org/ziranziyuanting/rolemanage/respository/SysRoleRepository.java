package org.ziranziyuanting.rolemanage.respository;


import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.ziranziyuanting.common.repository.CommonReactiveCrudRepository;
import org.ziranziyuanting.rolemanage.entity.SysRole;
import org.ziranziyuanting.rolemanage.model.SysMenu;

import reactor.core.publisher.Flux;

public interface SysRoleRepository extends CommonReactiveCrudRepository<SysRole>
{
    @Query("select re.ICON as icon, re.TITLE as title, re.name as 'name', re.MENU_TYPE as type, re.PATH as path, re.ID as id, re.PARENT_ID as pid, re.COMPONENT as component" +
            " from sys_relation r inner JOIN " + //
                "              sys_resource re ON " + //
                "              r.TARGET_ID = re.ID " + //
                "              where r.OBJECT_ID = :userId")
    Flux<SysMenu> findResourceWithUserId(@Param("userId") Long userId);
}
