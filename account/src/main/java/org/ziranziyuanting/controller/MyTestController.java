package org.ziranziyuanting.controller;

import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("mytest")
public class MyTestController {

    @GetMapping("test")
    public Mono<ResponseEntity<String>> test(){
        return Mono.just(ResponseEntity.ok("now: " + LocalDateTime.now()));
    }
}
