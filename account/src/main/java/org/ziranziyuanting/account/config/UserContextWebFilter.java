package org.ziranziyuanting.account.config;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class UserContextWebFilter implements WebFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        // 从 Gateway 传递的 Header 中获取用户信息
        String userId = exchange.getRequest().getHeaders().getFirst("X-User-Id");
        String userName = exchange.getRequest().getHeaders().getFirst("X-User-Name");
        String utf8UserName = URLDecoder.decode(userName, StandardCharsets.UTF_8);
        // 将用户信息存入 Reactor 的 Context 中，传递给后续的响应式链
        return chain.filter(exchange)
                .contextWrite(context -> context.put("userId", userId)
                        .put("userName", utf8UserName));
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE; // 保证最先执行
    }
}
