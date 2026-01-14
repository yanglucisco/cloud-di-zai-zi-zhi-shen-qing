package org.ziranziyuanting.authcenter.config.seurity;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.ziranziyuanting.authcenter.service.RegisteredClientService;
import org.ziranziyuanting.authcenter.utils.PasswordUtil;
import org.springframework.security.config.Customizer;

@Configuration
public class SecurityConfig {
    @SuppressWarnings("unused")
    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    @Order(1)
    SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
        http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                .oidc(Customizer.withDefaults()) // Enable OpenID Connect 1.0
        ;
        http
                // 配置异常处理，当需要认证时重定向到我们的自定义登录页
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/custom-login")))
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()
                // jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()) //
                // 应用自定义转换器
                ));

        return http.formLogin(Customizer.withDefaults()).build();
    }

    @Bean
    @Order(2)
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("/user/**").permitAll()
                        .requestMatchers("/login.html", "/css/**", "/js/**", "/images/**", "/favicon.ico").permitAll()
                        .requestMatchers("/custom-login", "/login").permitAll()
                        .requestMatchers("/home").permitAll()
                        .requestMatchers("/test/**").permitAll()
                        .anyRequest()
                        .authenticated())
                .csrf(csrf -> csrf.ignoringRequestMatchers("/test/**", "/user/**"))
                .formLogin(
                        formLogin -> formLogin
                                // 指定自定义登录页的URL
                                .loginPage("/custom-login")
                                // 指定处理登录认证的POST请求地址，与HTML表单action一致
                                .loginProcessingUrl("/login")
                );
        return http.build();
    }

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        // 设置 JWT 令牌中权限声明（claims）的名称，默认为 "scope" 或 "scopes"
        // 如果你的角色信息放在另一个claim里，修改这个值，例如 "roles"
        grantedAuthoritiesConverter.setAuthoritiesClaimName("authorities");
        // 设置权限前缀，如果不需要前缀可以设置为空字符串，这里设置为空以便直接使用自定义角色名
        grantedAuthoritiesConverter.setAuthorityPrefix("");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }
    @Bean
    PasswordEncoder PasswordEncoder(){
        return PasswordUtil.BCryptPasswordEncoder();
    }
    @Bean
    RegisteredClientRepository registeredClientRepository1(RegisteredClientService service) {
        return new RegisteredClientRepository() {
            @Override
            public void save(RegisteredClient registeredClient) {
                // service.save(registeredClient);
            }
            
            @Override
            public RegisteredClient findById(String id) {
                return null;// service.getById(1l).toOAuthClient();
            }
            
            @Override
            public RegisteredClient findByClientId(String clientId) {
                return service.findByClientId(clientId);
            }
        };
    }
}
