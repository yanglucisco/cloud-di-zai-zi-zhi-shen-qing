package org.ziranziyuanting.rolemanage.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ziranziyuanting.rolemanage.entity.SysResource;
import org.ziranziyuanting.rolemanage.param.SysMenuParam;
import org.ziranziyuanting.rolemanage.service.SysResourceService;

import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("sysmenu")
public class SysMenuController {
    private final SysResourceService sysResourceService;
    public SysMenuController(SysResourceService sysResourceService) {
        this.sysResourceService = sysResourceService;
    }
    @PostMapping("add")
    public ResponseEntity<Mono<String>> add(@Valid @RequestBody SysMenuParam sysMenuParam) {
        SysResource sysResource = SysResource.builder()
                .menuType(sysMenuParam.getMenuType())
                .visible(sysMenuParam.getVisible())
                .sortCode(sysMenuParam.getSortCode())
                .title(sysMenuParam.getTitle())
                .parentId(sysMenuParam.getParentId())
                .module(sysMenuParam.getModule())
                .path(sysMenuParam.getPath())
                .component(sysMenuParam.getComponent())
                .name(sysMenuParam.getName())
                .icon(sysMenuParam.getIcon())
                .build();
        return ResponseEntity.ok(sysResourceService.saveOrUpdate(sysResource).map(s -> "添加菜单成功"));
    }
}
