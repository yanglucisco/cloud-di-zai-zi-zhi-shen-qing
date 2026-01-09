package org.ziranziyuanting.rolemanage.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.ziranziyuanting.common.service.impl.CommonServiceImpl;
import org.ziranziyuanting.rolemanage.entity.SysRole;
import org.ziranziyuanting.rolemanage.model.SysMenu;
import org.ziranziyuanting.rolemanage.respository.SysRoleRepository;
import org.ziranziyuanting.rolemanage.service.SysRoleService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class SysRoleServiceImpl extends CommonServiceImpl<SysRole> implements SysRoleService {
    private final SysRoleRepository repository;
    public SysRoleServiceImpl(SysRoleRepository repository, PasswordEncoder passwordEncoder){
        super(repository);
        this.repository = repository;
    }
    // 基于表达式的权限控制
    // @PreAuthorize("hasAuthority('ROLE_ADMIN123123123')")
    @PreAuthorize("hasRole('ADMIN123123123')")
    @Override
    public Mono<String> test(Authentication authentication){
        return Mono.just("testtest");
    }
    @Override
    public Flux<SysMenu> findResourceWithUserId(Long userId) {
        return repository.findResourceWithUserId(userId).map(item -> {
            if(item.getType().equals("MULU")){
                item.setType("subMenu");
            }
            return item;
        }).collectList().flatMapMany(sysMenus -> {
            List<SysMenu> tree = new ArrayList<>();
            tree(sysMenus, tree);
            return Flux.fromIterable(tree);
        });
    }
    public void tree(List<SysMenu> sysMenus, List<SysMenu> tree){
        Map<Long, Optional<SysMenu>> maps = new HashMap<>();
        for (SysMenu sysMenu : sysMenus) {
            maps.put(sysMenu.getId(), Optional.of(sysMenu));
        }
        for (SysMenu sysMenu : sysMenus) {
            Long pid = sysMenu.getPid();
            if(pid == 0){
                tree.add(sysMenu);
            }
            else{
                maps.get(pid).ifPresent(parent -> parent.addSysMenu(sysMenu));
            }
        }
    }
}
