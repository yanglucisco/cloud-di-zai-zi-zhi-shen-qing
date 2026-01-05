package org.ziranziyuanting.rolemanage.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.HttpStatusServerEntryPoint;
import org.ziranziyuanting.rolemanage.util.PasswordUtil;

@Configuration
@EnableReactiveMethodSecurity
public class SecurityConfig {
        private final CustomJwtAuthenticationConverter customJwtAuthenticationConverter;

        public SecurityConfig(CustomJwtAuthenticationConverter customJwtAuthenticationConverter) {
                this.customJwtAuthenticationConverter = customJwtAuthenticationConverter;
        }

        @Bean
        PasswordEncoder PasswordEncoder() {
                return PasswordUtil.BCryptPasswordEncoder();
        }

        @Bean
        SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) throws Exception {
                http
                                .csrf(csrf -> csrf.disable())
                                .formLogin(fLogin -> fLogin.disable())
                                .httpBasic(hBasic -> hBasic.disable())
                                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt
                                                .jwtAuthenticationConverter(customJwtAuthenticationConverter)))
                                .authorizeExchange(exchange -> exchange
                                                .pathMatchers("/sysrole/**").hasRole("ADMIN123123123")
                                                .anyExchange().authenticated())
                                // 前后端分离项目，请求后端数据时，不应该返回302，而是应该返回401
                                .exceptionHandling(exceptionHandling -> exceptionHandling
                                                .authenticationEntryPoint(new HttpStatusServerEntryPoint(
                                                                HttpStatus.UNAUTHORIZED)));
                return http.build();
        }
}
