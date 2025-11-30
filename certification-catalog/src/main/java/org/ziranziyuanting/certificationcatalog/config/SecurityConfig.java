package org.ziranziyuanting.certificationcatalog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
// import org.springframework.security.web.server.authentication.HttpStatusServerEntryPoint;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                                // .requestMatchers("/test/**").permitAll()
                                // .requestMatchers("/user/**").permitAll()
                                .anyRequest().authenticated()
                        )
                        // 前后端分离项目，请求后端数据时，不应该返回302，而是应该返回401
                        // 只配置这里就行，其它地方不用配置
                        .exceptionHandling(exceptionHandling -> exceptionHandling
                                                .authenticationEntryPoint(new HttpStatusEntryPoint(
                                    HttpStatus.UNAUTHORIZED))
                        )
                .oauth2Login(withDefaults()
                        // oauth2Login -> oauth2Login.loginPage("/oauth2/authorization/certification-catalog-oidc")
                )
                .oauth2Client(withDefaults());
        return http.build();
    }
}
