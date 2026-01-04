package org.ziranziyuanting.rolemanage.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ziranziyuanting.rolemanage.entity.SysResource;
import org.ziranziyuanting.rolemanage.param.SysModuleParam;
import org.ziranziyuanting.rolemanage.service.SysResourceService;

import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("sysmodule")
public class SysModuleController {
    private final SysResourceService sysResourceService;
    public SysModuleController(SysResourceService sysResourceService) {
        this.sysResourceService = sysResourceService;
    }
    @PostMapping("add")
    public ResponseEntity<Mono<String>> add(@Valid @RequestBody SysModuleParam param) {
        SysResource sysResource = SysResource.builder()
            .sortCode(param.getSortCode())
            .title(param.getTitle())
            .icon(param.getIcon())
            .color(param.getColor())
            .build();
        return ResponseEntity.ok(sysResourceService.saveOrUpdate(sysResource).map(s -> "新增模块成功"));
    }
    
}
