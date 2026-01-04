package org.ziranziyuanting.rolemanage.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ziranziyuanting.rolemanage.entity.SysRole;
import org.ziranziyuanting.rolemanage.param.SysRoleParam;
import org.ziranziyuanting.rolemanage.service.SysRoleService;

import jakarta.validation.Valid;
import lombok.val;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("sysrole")
public class SysRoleController {
    private final SysRoleService sysRoleService;
    public SysRoleController(SysRoleService sysRoleService) {
        this.sysRoleService = sysRoleService;
    }
    @GetMapping("findall")
    public ResponseEntity<Flux<SysRole>> findall() {
        return ResponseEntity.ok(sysRoleService.findAll());
    }
    @PostMapping("add")
    public ResponseEntity<Mono<String>> add(@Valid @RequestBody SysRoleParam param) {
        SysRole sysRole = SysRole.builder()
            .name(param.getName())
            .category(param.getCategory())
            .sortCode(param.getSortCode())
            .build();
        return ResponseEntity.ok(sysRoleService.saveOrUpdate(sysRole).map(s -> "新增角色成功!"));
    }
    
    
}
