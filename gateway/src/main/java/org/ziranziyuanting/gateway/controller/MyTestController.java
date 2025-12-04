package org.ziranziyuanting.gateway.controller;

import java.time.LocalDateTime;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("mytest")
public class MyTestController {
    @GetMapping("test")
    public Mono<String> test(Authentication authentication, ServerHttpRequest request){
        var authHeader = request.getHeaders();
        return Mono.just(LocalDateTime.now().toString());
    }
}
