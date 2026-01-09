package org.ziranziyuanting.rolemanage.service;

import org.springframework.security.core.Authentication;
import org.ziranziyuanting.common.service.CommonService;
import org.ziranziyuanting.rolemanage.entity.SysRole;
import org.ziranziyuanting.rolemanage.model.SysMenu;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SysRoleService extends CommonService<SysRole>
{
    Mono<String> test(Authentication authentication);  
    Flux<SysMenu> findResourceWithUserId(Long userId); 
}
