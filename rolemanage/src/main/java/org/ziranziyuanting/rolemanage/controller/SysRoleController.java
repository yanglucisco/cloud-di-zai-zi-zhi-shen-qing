package org.ziranziyuanting.rolemanage.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ziranziyuanting.rolemanage.entity.SysRole;
import org.ziranziyuanting.rolemanage.service.SysRoleService;

import reactor.core.publisher.Flux;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;


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
    
}
