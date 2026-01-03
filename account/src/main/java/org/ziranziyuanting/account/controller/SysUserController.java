package org.ziranziyuanting.account.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ziranziyuanting.account.param.SysUserParam;
import org.ziranziyuanting.account.service.SysUserService;

import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/sysUser")
public class SysUserController {
    private final SysUserService sysUserService;
    public SysUserController(SysUserService sysUserService) {
        this.sysUserService = sysUserService;
    }
    @PostMapping("add")
    public ResponseEntity<Mono<String>> add(@Valid @RequestBody SysUserParam param) {
        return ResponseEntity.ok(sysUserService.add(param));
    }
    
}
