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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;




@RestController
@RequestMapping("org")
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
    public ResponseEntity<String> add(@Valid @RequestBody AddOrgParam addOrgParam){
        Mono<SysOrg> r = service.save(addOrgParam);
        return ResponseEntity.ok("新增成功: " + r.block().getName());
    }
    @GetMapping("test")
    public String test() {
        service.test();
        return LocalDateTime.now().toString();
    }
}
