package org.ziranziyuanting.account.controller;

import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ziranziyuanting.account.config.ReactiveUserContext;

import reactor.core.publisher.Mono;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
@RequestMapping("mytest")
public class MyTestController {

    @GetMapping("test")
    public ResponseEntity<Mono<String>> test(){
        return ResponseEntity.ok(ReactiveUserContext.getUserId().flatMap(userId -> {
            return Mono.just("userId: " + userId);
        }));
        // return Mono.just(ResponseEntity.ok("now: " + LocalDateTime.now()));
    }
    @PostMapping("test")
    public Mono<ResponseEntity<String>> testPost(){
        return Mono.just(ResponseEntity.ok("testPost now: " + LocalDateTime.now()));
    }
}
