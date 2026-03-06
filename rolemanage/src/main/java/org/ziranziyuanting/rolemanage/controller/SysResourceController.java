package org.ziranziyuanting.rolemanage.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import org.ziranziyuanting.rolemanage.entity.SysResource;
import org.ziranziyuanting.rolemanage.service.SysResourceService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("sysresource")
public class SysResourceController {
    private final SysResourceService sysResourceService;
    private final WebClient webClient;
    public SysResourceController(SysResourceService sysResourceService, WebClient webClient) {
        this.sysResourceService = sysResourceService;
        this.webClient = webClient;
    }
    @GetMapping("all")
    public ResponseEntity<Flux<SysResource>> all() {
        return ResponseEntity.ok(sysResourceService.findAll());
    }
    @GetMapping("test")
    public ResponseEntity<Mono<String>> test(ServerWebExchange exchange) {
        // 获取前端请求的 headers
        HttpHeaders incomingHeaders = exchange.getRequest().getHeaders();
        Mono<String> r = 
         webClient.get()
            .uri("/mytest/test")
            .headers(headers -> {
                // 传递特定的 headers
                copyHeaders(incomingHeaders, headers, 
                    HttpHeaders.AUTHORIZATION,
                    HttpHeaders.USER_AGENT,
                    "X-Request-ID",
                    "X-Correlation-ID"
                );
            })
            .retrieve()
            .bodyToMono(String.class);
        //Mono.just(LocalDateTime.now()+"");
        return ResponseEntity.ok(r);
    }
    @SuppressWarnings("null")
    private void copyHeaders(HttpHeaders source, org.springframework.http.HttpHeaders target, 
                            String... headerNames) {
        for (String headerName : headerNames) {
            List<String> values = source.get(headerName);
            if (values != null && !values.isEmpty()) {
                target.addAll(headerName, values);
            }
        }
    }
}
