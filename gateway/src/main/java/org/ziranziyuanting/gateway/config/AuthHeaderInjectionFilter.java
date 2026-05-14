package org.ziranziyuanting.gateway.config;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@Component
public class AuthHeaderInjectionFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // Use ReactiveSecurityContextHolder to get the authentication reactively
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .filter(Authentication::isAuthenticated)
                .flatMap(authentication -> {
                    // 1. Extract Roles and Permissions
                    String authorities = authentication.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .collect(Collectors.joining(","));

                    String username = authentication.getName();
                    String utf8UserName = URLEncoder.encode(username, StandardCharsets.UTF_8);
                    String userId = null;
                    if (authentication instanceof JwtAuthenticationToken) {
                        Jwt jwt = ((JwtAuthenticationToken) authentication).getToken();
                        // Example: Get a custom claim named "user_id"
                        userId = jwt.getClaimAsString("user_id");
                        // Or get the raw token value if absolutely necessary (rarely recommended for
                        // headers)
                        // String tokenValue = jwt.getTokenValue();
                    }
                    // 2. Mutate the Request to Add Custom Headers
                    ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                            .header("X-User-Id", userId)
                            .header("X-User-Name", utf8UserName)
                            .header("X-User-Roles", authorities)
                            .build();

                    // 3. Continue the filter chain with the mutated request
                    return chain.filter(exchange.mutate().request(mutatedRequest).build());
                })
                .switchIfEmpty(chain.filter(exchange)) // If not authenticated, proceed without headers
        ;
    }

    @Override
    public int getOrder() {
        // This filter should run after authentication is established.
        // Spring Security's AuthenticationWebFilter runs at order -2.
        // Setting this to -1 ensures it runs after security filters but before routing.
        return -1;
    }
}