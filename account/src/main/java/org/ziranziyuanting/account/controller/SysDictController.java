package org.ziranziyuanting.account.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ziranziyuanting.account.entity.SysDict;
import org.ziranziyuanting.account.param.AddDictParam;
import org.ziranziyuanting.account.service.SysDictService;
import org.ziranziyuanting.account.vo.SysDictVO;

import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RequestMapping("dict")
@RestController
public class SysDictController {
    private final SysDictService service;
    public SysDictController(SysDictService sysDictService)
    {
        this.service = sysDictService;
    }
    @GetMapping("findAll")
    public ResponseEntity<Mono<SysDictVO>> findAll() {
        return ResponseEntity.ok(service.findAllVO());
    }
    @PostMapping("add")
    public ResponseEntity<Mono<String>> add(@Valid @RequestBody AddDictParam dict) {
        SysDict sysDict = SysDict.builder().parentId(dict.getParentId())
                .dictLabel(dict.getName())
                .dictValue(dict.getCode())
                .category(dict.getType())
                .sortCode(dict.getSortCode())
                .build();
        return ResponseEntity.ok(Mono.just(sysDict).flatMap(service::saveOrUpdate).map(savedDict -> {
            return "新增字典成功: " + savedDict.getId();
        }));
    }
}
