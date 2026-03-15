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
        private final SecurityProperties securityProperties;

        public SecurityConfig(CustomJwtAuthenticationConverter customJwtAuthenticationConverter, SecurityProperties securityProperties) {
                this.customJwtAuthenticationConverter = customJwtAuthenticationConverter;
                this.securityProperties = securityProperties;
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
                                                // .pathMatchers("/sysrole/**").hasRole("ADMIN123123123")
                                                // .pathMatchers("/mytest/**").permitAll()
                                                // 从配置文件中读取公开路径
                                                .pathMatchers(securityProperties.getPermitAll().toArray(new String[0]))
                                                .permitAll()
                                                // 从配置文件中读取需要认证的路径
                                                .pathMatchers(securityProperties.getAuthenticated().toArray(new String[0]))
                                                .authenticated()
                                                // 从配置文件中读取需要角色的路径
                                                .pathMatchers(securityProperties.getAdminPaths().toArray(new String[0]))
                                                .hasRole("ADMIN123123123")
                                                .anyExchange().authenticated())
                                // 前后端分离项目，请求后端数据时，不应该返回302，而是应该返回401
                                .exceptionHandling(exceptionHandling -> exceptionHandling
                                                .authenticationEntryPoint(new HttpStatusServerEntryPoint(
                                                                HttpStatus.UNAUTHORIZED)));
                return http.build();
        }
}
