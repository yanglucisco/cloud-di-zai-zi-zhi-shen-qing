package org.ziranziyuanting.certificationcatalog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        // .requestMatchers("/test/**").permitAll()
                        .requestMatchers("/user/**").permitAll()
                        .anyRequest().authenticated())
                .oauth2Login(withDefaults()
                        // oauth2Login -> oauth2Login.loginPage("/oauth2/authorization/certification-catalog-oidc")
                )
                .oauth2Client(withDefaults());
        return http.build();
    }
}
