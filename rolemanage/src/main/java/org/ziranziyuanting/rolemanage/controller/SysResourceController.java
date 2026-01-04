package org.ziranziyuanting.rolemanage.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ziranziyuanting.rolemanage.entity.SysResource;
import org.ziranziyuanting.rolemanage.service.SysResourceService;

import reactor.core.publisher.Flux;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("sysresource")
public class SysResourceController {
    private final SysResourceService sysResourceService;
    public SysResourceController(SysResourceService sysResourceService) {
        this.sysResourceService = sysResourceService;
    }
    @GetMapping("all")
    public ResponseEntity<Flux<SysResource>> all() {
        return ResponseEntity.ok(sysResourceService.findAll());
    }
    
}
