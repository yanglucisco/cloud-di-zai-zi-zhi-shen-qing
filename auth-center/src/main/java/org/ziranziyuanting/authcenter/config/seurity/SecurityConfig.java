package org.ziranziyuanting.authcenter.config.seurity;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.config.Customizer;

@Configuration
public class SecurityConfig {
    @SuppressWarnings("unused")
    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

//     @Bean
//     @Order(1)
//     SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
//         OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
//         http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
//                 .oidc(Customizer.withDefaults()) // Enable OpenID Connect 1.0
//         ;
//         http
//                 // 配置异常处理，当需要认证时重定向到我们的自定义登录页
//                 .exceptionHandling(exceptions -> exceptions
//                         .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/custom-login")))
//                 .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()
//                 // jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()) //
//                 // 应用自定义转换器
//                 ));

//         return http.formLogin(Customizer.withDefaults()).build();
//     }

    @Bean
    @Order(2)
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        // .requestMatchers("/test/**").permitAll()
                        .requestMatchers("/login.html", "/css/**", "/js/**", "/images/**", "/favicon.ico").permitAll()
                        .requestMatchers("/custom-login", "/login").permitAll()
                        .requestMatchers("/home").permitAll()
                        .anyRequest()
                        .authenticated())
                .formLogin(
                        formLogin -> formLogin
                                // 指定自定义登录页的URL
                                .loginPage("/custom-login")
                                // 指定处理登录认证的POST请求地址，与HTML表单action一致
                                .loginProcessingUrl("/login")
                // 登录成功后的默认跳转页面
                // .defaultSuccessUrl("/home", true)
                );
        return http.build();
    }
}
