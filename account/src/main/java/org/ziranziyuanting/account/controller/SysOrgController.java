package org.ziranziyuanting.account.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ziranziyuanting.account.entity.SysOrg;
import org.ziranziyuanting.account.param.AddOrgParam;
import org.ziranziyuanting.account.service.SysOrgService;

import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("org")
@Validated
public class SysOrgController {
    private final SysOrgService service;
    public SysOrgController(SysOrgService sysOrgService)
    {
        this.service = sysOrgService;
    }
    @GetMapping("all")
    public ResponseEntity<Flux<SysOrg>> all() {
        return ResponseEntity.ok(service.findAll());
    }
    @PostMapping("add")
    public ResponseEntity<Mono<String>> add(@Valid @RequestBody AddOrgParam addOrgParam, Authentication authentication) {
        return ResponseEntity.ok(service.save(addOrgParam).map(s -> "新增机构成功"));
    }
    @GetMapping("test")
    public String test() {
        service.test();
        return LocalDateTime.now().toString();
    }
}
