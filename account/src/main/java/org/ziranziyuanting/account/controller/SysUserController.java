package org.ziranziyuanting.account.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ziranziyuanting.account.config.ReactiveUserContext;
import org.ziranziyuanting.account.param.SysUserParam;
import org.ziranziyuanting.account.param.SysUserUpdateParam;
import org.ziranziyuanting.account.param.UpdatePassParam;
import org.ziranziyuanting.account.service.SysUserService;
import org.ziranziyuanting.account.vo.SysUserVO;

import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/sysUser")
public class SysUserController {
    private final SysUserService sysUserService;
    public SysUserController(SysUserService sysUserService) {
        this.sysUserService = sysUserService;
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
            sysUserService.findById(userId).map(user ->
                SysUserVO.builder()
                    .account(user.getAccount())
                    .name(user.getName())
                    .nickName(user.getNickname())
                    .gender(user.getGender())
                    .email(user.getEmail())
                    .mobil(user.getPhone())
                    .birthday(user.getBirthday())
                    .build()
            )
        ));
    }
    @PostMapping("update")
    public ResponseEntity<Mono<String>> update(@RequestBody SysUserUpdateParam param) {
        return ResponseEntity.ok(sysUserService.update(param));
    }
}

