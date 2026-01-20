package org.ziranziyuanting.rolemanage.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.ziranziyuanting.rolemanage.entity.SysRelation;
import org.ziranziyuanting.rolemanage.entity.SysRole;
import org.ziranziyuanting.rolemanage.model.SysMenu;
import org.ziranziyuanting.rolemanage.param.SysRoleParam;
import org.ziranziyuanting.rolemanage.param.SysRoleShouQuanCaiDanParam;
import org.ziranziyuanting.rolemanage.service.SysRelationService;
import org.ziranziyuanting.rolemanage.service.SysRoleService;

import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("sysrole")
public class SysRoleController {
    private final SysRoleService sysRoleService;
    private final SysRelationService sysRelationService;
    public SysRoleController(SysRoleService sysRoleService, SysRelationService sysRelationService) {
        this.sysRoleService = sysRoleService;
        this.sysRelationService = sysRelationService;
    }
    @GetMapping("findall")
    public ResponseEntity<Flux<SysRole>> findall() {
        return ResponseEntity.ok(sysRoleService.findAll());
    }
    @PostMapping("add")
    public ResponseEntity<Mono<String>> add(@Valid @RequestBody SysRoleParam param, Authentication authentication) {
        SysRole sysRole = SysRole.builder()
            .name(param.getName())
            .category(param.getCategory())
            .sortCode(param.getSortCode())
            .build();
        // return ResponseEntity.ok(sysRoleService.test(authentication));
        return ResponseEntity.ok(sysRoleService.saveOrUpdate(sysRole).map(s -> "新增角色成功!"));
    }
    @PostMapping("shouQuanCaiDan")
    public ResponseEntity<Mono<String>> shouQuanCaiDan(@Valid @RequestBody SysRoleShouQuanCaiDanParam param) {
        return ResponseEntity.ok(sysRelationService.shouQuanCaiDan(param).map(s -> "新增授权成功"));
    }
    @GetMapping("get")
    public ResponseEntity<Flux<SysMenu>> get() {
        return ResponseEntity.ok(sysRoleService.findResourceWithUserId(1l));
    }
}
