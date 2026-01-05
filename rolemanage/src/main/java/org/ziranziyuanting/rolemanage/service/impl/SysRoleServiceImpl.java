package org.ziranziyuanting.rolemanage.service.impl;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.ziranziyuanting.common.service.impl.CommonServiceImpl;
import org.ziranziyuanting.rolemanage.entity.SysRole;
import org.ziranziyuanting.rolemanage.respository.SysRoleRepository;
import org.ziranziyuanting.rolemanage.service.SysRoleService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class SysRoleServiceImpl extends CommonServiceImpl<SysRole> implements SysRoleService {
    public SysRoleServiceImpl(SysRoleRepository repository, PasswordEncoder passwordEncoder){
        super(repository);
    }
    // 基于表达式的权限控制
    // @PreAuthorize("hasAuthority('ROLE_ADMIN123123123')")
    @PreAuthorize("hasRole('ADMIN123123123')")
    @Override
    public Mono<String> test(Authentication authentication){
        return Mono.just("testtest");
    }
}
