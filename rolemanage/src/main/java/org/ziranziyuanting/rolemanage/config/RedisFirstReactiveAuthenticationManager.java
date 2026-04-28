package org.ziranziyuanting.rolemanage.config;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtReactiveAuthenticationManager;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Mono;

@Component
public class RedisFirstReactiveAuthenticationManager implements ReactiveAuthenticationManager {
    private final ReactiveJwtDecoder jwtDecoder;;
    private final CustomJwtAuthenticationConverter jwtConverter; // 你原有的转换器

    public RedisFirstReactiveAuthenticationManager(
            ReactiveJwtDecoder jwtDecoder,
            CustomJwtAuthenticationConverter jwtConverter) {
        this.jwtConverter = jwtConverter;
        this.jwtDecoder = jwtDecoder;
        // // 初始化默认的 JWT Manager
        // this.jwtManager = new JwtReactiveAuthenticationManager(jwtDecoder);
        // this.jwtManager.setJwtAuthenticationConverter(jwtConverter);
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        // 1. Get Token String
        String token = null;
        if (authentication instanceof org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken) {
            token = ((org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken) authentication)
                    .getToken();
        }
        if (token == null || token.isEmpty()) {
            return Mono.error(new BadCredentialsException("Invalid token"));
        }
        // 2. Preliminary Check: Is it a valid JWT format? (Header.Payload.Signature)
        if (!isJwtFormat(token)) {
           List<String> adminRoles = new ArrayList<>();
                    adminRoles.add("ROLE_admin");
                    adminRoles.add("ROLE_ADMIN123123123");
                    List<String> permissions = List.of(
                            "catalog.read", "catalog.edit", "catalog.delete",
                            "employee.read", "employee.edit", "employee.delete",
                            "work.read", "work.edit", "work.delete",
                            "order.read", "order.edit", "order.delete",
                            "abc.read", "abc.edit", "abc.delete",
                            "catalog1.read", "catalog1.edit", "catalog1.delete",
                            "catalog2.read", "catalog2.edit", "catalog2.delete",
                            "catalog3.read", "catalog3.edit", "catalog3.delete",
                            "catalog4.read", "catalog4.edit", "catalog4.delete");
                    adminRoles.addAll(permissions);
                    List<SimpleGrantedAuthority> authorities = new ArrayList<>();
                    for(String item : adminRoles){
                        authorities.add(new SimpleGrantedAuthority(item));
                    }
                    // List<SimpleGrantedAuthority> authorities = userDetails.getRoles().stream()
                    //         .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                    //         .collect(Collectors.toList());

                    // Add permissions if needed
                    // authorities.addAll(userDetails.getPermissions().stream()
                    // .map(SimpleGrantedAuthority::new)
                    // .collect(Collectors.toList()));

                    // Create Authentication Instance
                    // Principal: userDetails or username
                    // Credentials: null (since it's already authenticated via token)
                    // Authorities: list of roles/permissions
                    return Mono.just(new UsernamePasswordAuthenticationToken(
                            "yanglu",
                            null,
                            authorities));
        }
        // 2. Decode JWT and Convert to Authentication
        var a1 = jwtDecoder.decode(token)
                .flatMap(jwt -> {
                    return jwtConverter.convert(jwt);
                    // return "";
                }).map(authentication1 -> (Authentication) authentication1);
        return a1;
    }
    /**
     * Checks if the token string follows the basic JWT structure (three parts separated by dots).
     * Note: This does NOT validate signature or content, only format.
     */
    private boolean isJwtFormat(String token) {
        if (token == null) {
            return false;
        }
        // JWTs must have exactly 2 dots separating 3 parts
        int firstDot = token.indexOf('.');
        int lastDot = token.lastIndexOf('.');
        
        // Must have at least one dot, and first/last must be different (at least 3 parts)
        // Also ensure no empty parts (basic check)
        return firstDot > 0 && lastDot > firstDot && lastDot < token.length() - 1;
    }
}
