package org.ziranziyuanting.rolemanage.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ziranziyuanting.rolemanage.entity.SysResource;
import org.ziranziyuanting.rolemanage.param.SysButtonParam;
import org.ziranziyuanting.rolemanage.service.SysResourceService;

import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("sysbtn")
public class SysButtonController {
    private final SysResourceService sysResourceService;
    public SysButtonController(SysResourceService sysResourceService){
        this.sysResourceService = sysResourceService;
    }
    @PostMapping("add")
    public ResponseEntity<Mono<String>> add(@Valid @RequestBody SysButtonParam param) {
        SysResource sysResource = SysResource.builder().name(param.getName())
        .code(param.getCode()).parentId(param.getParentId()).sortCode(param.getSortCode()).build();
        return ResponseEntity.ok(sysResourceService.saveOrUpdate(sysResource).map(s -> "新增按钮成功"));
    }
    
}
