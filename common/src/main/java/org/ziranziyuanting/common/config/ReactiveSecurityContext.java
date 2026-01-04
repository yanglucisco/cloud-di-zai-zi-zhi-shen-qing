package org.ziranziyuanting.common.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.jwt.Jwt;

import reactor.core.publisher.Mono;

public class ReactiveSecurityContext {
    /**
     * 获取当前认证信息
     */
    public static Mono<Authentication> getCurrentAuthentication() {
        return ReactiveSecurityContextHolder.getContext()
            .map(SecurityContext::getAuthentication);
    }
    /**
     * 获取当前用户ID
     */
    public static Mono<Long> getCurrentUserId() {
        return getCurrentAuthentication()
            .flatMap(auth -> {
                // 从JWT Token获取
                if (auth.getPrincipal() instanceof Jwt) {
                    Jwt jwt = (Jwt) auth.getPrincipal();
                    return extractUserIdFromJwt(jwt);
                }
                // 从JwtAuthenticationToken获取
                // else if (auth instanceof JwtAuthenticationToken) {
                //     Jwt jwt = ((JwtAuthenticationToken) auth).getToken();
                //     return extractUserIdFromJwt(jwt);
                // }
                // // 从CustomUserDetails获取
                // else if (auth.getPrincipal() instanceof CustomUserDetails) {
                //     CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
                //     return Mono.just(userDetails.getUserId());
                // }
                // // 从用户名获取（如果是简单的用户名认证）
                // else if (auth.getPrincipal() instanceof String) {
                //     return Mono.empty(); // 需要根据业务逻辑处理
                // }
                return Mono.empty();
            });
    }
    /**
     * 从JWT中提取用户ID
     */
    private static Mono<Long> extractUserIdFromJwt(Jwt jwt) {
        Object userIdObj = jwt.getClaim("user_id");
        if (userIdObj instanceof Number) {
            return Mono.just(((Number) userIdObj).longValue());
        } else if (userIdObj instanceof String) {
            try {
                return Mono.just(Long.parseLong((String) userIdObj));
            } catch (NumberFormatException e) {
                return Mono.empty();
            }
        }
        return Mono.empty();
    }
}
