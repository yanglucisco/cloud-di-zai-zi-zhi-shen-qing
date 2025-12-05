package org.ziranziyuanting.gateway.controller;

import java.time.LocalDateTime;

import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("user")
public class UserController {
    @GetMapping("current")
    public Mono<String> currentUserName(Authentication authentication){
        var principal = (Jwt)authentication.getPrincipal();
        var userId = principal.getClaim("user_id");
        //从authentication中解析出用户ID，然后从数据库获取用户信息
        return Mono.just("用户名" + LocalDateTime.now());
    }
}
