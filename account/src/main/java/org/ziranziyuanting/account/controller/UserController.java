package org.ziranziyuanting.account.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ziranziyuanting.account.entity.YlTest;
import org.ziranziyuanting.account.param.UserRegistParam;
import org.ziranziyuanting.account.service.YlTestService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/user")
public class UserController {
    @SuppressWarnings("unused")
    private final PasswordEncoder passwordEncoder;
    private final YlTestService ylTestService;

    public UserController(PasswordEncoder passwordEncoder, YlTestService ylTestService) {
        this.passwordEncoder = passwordEncoder;
        this.ylTestService = ylTestService;
    }

    @PostMapping("regist")
    public Mono<ResponseEntity<String>> regist(@RequestBody UserRegistParam userRegistParam) {
        // {bcrypt}$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG
        return Mono.just(ResponseEntity.ok("post注册成功: " + userRegistParam.getUserName()));
    }
    @GetMapping("all")
    public ResponseEntity<Flux<YlTest>> all() {
       Flux<YlTest> r = ylTestService.all();
        return ResponseEntity.ok(r);
    }
    @GetMapping("test")
    public ResponseEntity<Mono<String>> test() {
        return ResponseEntity.ok(Mono.just("ceshiceshiceshi"));
    }
}
