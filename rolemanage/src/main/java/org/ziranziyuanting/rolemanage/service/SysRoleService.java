package org.ziranziyuanting.rolemanage.service;

import org.springframework.security.core.Authentication;
import org.ziranziyuanting.common.service.CommonService;
import org.ziranziyuanting.rolemanage.entity.SysRole;

import reactor.core.publisher.Mono;

public interface SysRoleService extends CommonService<SysRole>
{

    Mono<String> test(Authentication authentication);   
}
