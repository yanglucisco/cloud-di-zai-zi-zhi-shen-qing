package org.ziranziyuanting.account.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ziranziyuanting.account.entity.SysPosition;
import org.ziranziyuanting.account.param.SysPositionParam;
import org.ziranziyuanting.account.service.SysPositionService;

import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("position")
public class SysPositionController {
    private final SysPositionService service;
    public SysPositionController(SysPositionService sysPositionService)
    {
        this.service = sysPositionService;
    }
    @PostMapping("add")
    public ResponseEntity<Mono<String>> add(@Valid @RequestBody SysPositionParam param) {
        return ResponseEntity.ok(Mono.just(param).flatMap(p -> {
            SysPosition position = org.ziranziyuanting.account.entity.SysPosition.builder()
                    .orgId(p.getOrgId())
                    .name(p.getName())
                    .category(p.getCategory())
                    .sortCode(p.getSortCode())
                    .build();
            return service.saveOrUpdate(position);
            
        }).map(savedPosition -> {
            return "新增职位成功: " + savedPosition.getId();
        }));
    }
}