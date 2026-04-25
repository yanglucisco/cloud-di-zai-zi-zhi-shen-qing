package org.ziranziyuanting.account.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ziranziyuanting.account.entity.SysOrg;
import org.ziranziyuanting.account.param.AddOrgParam;
import org.ziranziyuanting.account.param.PageParam;
import org.ziranziyuanting.account.service.SysOrgService;
import org.ziranziyuanting.account.vo.SysOrgVO;

import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.PageRequest;
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
    @GetMapping("orgTree")
    public ResponseEntity<Flux<SysOrgVO>> orgTree() {
        return ResponseEntity.ok(service.orgTree());
    }
    @GetMapping("page")
    public ResponseEntity<Mono<Map<String, Object>>> getOrgsByPage(@Valid PageParam pageParam) {
        pageParam.setPage(pageParam.getPage() - 1);
        Mono<Map<String, Object>> result = Mono.zip(
            service.findOrgsByPage(pageParam).collectList(),
            service.countOrgs()
        ).map(tuple -> {
            Map<String, Object> map = new HashMap<>();
            map.put("list", tuple.getT1());
            map.put("total", tuple.getT2());
            map.put("page", pageParam.getPage() + 1);
            map.put("size", pageParam.getPageSize());
            return map;
        });

        return ResponseEntity.ok(result);
    }
}
