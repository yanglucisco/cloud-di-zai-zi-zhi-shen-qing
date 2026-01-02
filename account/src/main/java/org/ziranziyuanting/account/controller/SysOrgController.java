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
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.jwt.Jwt;
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
    public ResponseEntity<Mono<SysOrg>> add(@Valid @RequestBody AddOrgParam addOrgParam, Authentication authentication) {
        // var r = ReactiveSecurityContextHolder.getContext()
        //     .map(SecurityContext::getAuthentication).map(s -> {
        //         System.out.println("当前认证信息: " + s);
        //         var sget = (Jwt)(s.getPrincipal());
        //         String user_id1 = sget.getClaimAsString("user_id");
        //         System.out.println("当前用户ID: " + user_id1);
        //         Jwt jwt = (Jwt)(authentication.getPrincipal());
        //         String user_id = jwt.getClaimAsString("user_id");
        //         service.save(addOrgParam);
        //         return "新增成功: " + LocalDateTime.now();
        //     });
        return ResponseEntity.ok(service.save(addOrgParam));
    }
    @GetMapping("test")
    public String test() {
        service.test();
        return LocalDateTime.now().toString();
    }
}
