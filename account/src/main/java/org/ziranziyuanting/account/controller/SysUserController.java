package org.ziranziyuanting.account.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ziranziyuanting.account.config.ReactiveUserContext;
import org.ziranziyuanting.account.param.SysUserPageParam;
import org.ziranziyuanting.account.param.SysUserParam;
import org.ziranziyuanting.account.param.SysUserStatusParam;
import org.ziranziyuanting.account.param.DeleteUserParam;
import org.ziranziyuanting.account.param.SysUserUpdateParam;
import org.ziranziyuanting.account.param.UpdatePassParam;
import org.ziranziyuanting.account.repository.SysOrgRepository;
import org.ziranziyuanting.account.service.SysUserService;
import org.ziranziyuanting.account.vo.SysUserVO;

import jakarta.validation.Valid;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping("/sysUser")
@Validated
public class SysUserController {
    private final SysUserService sysUserService;
    private final SysOrgRepository sysOrgRepository;
    public SysUserController(SysUserService sysUserService, SysOrgRepository sysOrgRepository) {
        this.sysUserService = sysUserService;
        this.sysOrgRepository = sysOrgRepository;
    }
    @PostMapping("add")
    public ResponseEntity<Mono<String>> add(@Valid @RequestBody SysUserParam param) {
        return ResponseEntity.ok(sysUserService.add(param));
    }
    @PostMapping("updatePassword")
    public ResponseEntity<Mono<String>> updatePassword(@RequestBody UpdatePassParam updatePassParam) {
        
        return ResponseEntity.ok(sysUserService.updatePassword(updatePassParam.getPassword()));
    }
    @GetMapping("getCurrentUser")
    public ResponseEntity<Mono<SysUserVO>> getCurrentUser(){
        return ResponseEntity.ok(ReactiveUserContext.getUserId().flatMap(userId ->
            sysUserService.findById(userId).flatMap(user -> {
                Mono<String> orgNameMono = user.getOrgId() != null
                    ? sysOrgRepository.findById(user.getOrgId()).map(org -> org.getName()).defaultIfEmpty("")
                    : Mono.just("");
                return orgNameMono.map(orgName ->
                    SysUserVO.builder()
                        .account(user.getAccount())
                        .name(user.getName())
                        .nickName(user.getNickname())
                        .gender(user.getGender())
                        .email(user.getEmail())
                        .mobil(user.getPhone())
                        .birthday(user.getBirthday())
                        .avatar(user.getAvatar())
                        .orgName(orgName)
                        .build()
                );
            })
        ));
    }
    @PostMapping("update")
    public ResponseEntity<Mono<String>> update(@RequestBody SysUserUpdateParam param) {
        return ResponseEntity.ok(sysUserService.update(param));
    }

    @GetMapping("page")
    public ResponseEntity<Mono<Map<String, Object>>> getUserPage(@Valid SysUserPageParam pageParam) {
        // 前端传 1-based 页码，转为 0-based
        int dbPage = pageParam.getPage() - 1;
        pageParam.setPage(dbPage < 0 ? 0 : dbPage);
        Mono<Map<String, Object>> result = Mono.zip(
                sysUserService.findUsersByPage(pageParam).collectList(),
                sysUserService.countUsers(pageParam)
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

    @PostMapping("updateStatus")
    public ResponseEntity<Mono<String>> updateStatus(@Valid @RequestBody SysUserStatusParam param) {
        return ResponseEntity.ok(sysUserService.updateStatus(param));
    }

    @PostMapping("delete")
    public ResponseEntity<Mono<String>> delete(@RequestBody DeleteUserParam param) {
        return ResponseEntity.ok(sysUserService.delete(param.getIds()));
    }
}

