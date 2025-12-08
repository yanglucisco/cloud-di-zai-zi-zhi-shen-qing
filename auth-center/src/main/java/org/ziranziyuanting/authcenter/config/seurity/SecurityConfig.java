package org.ziranziyuanting.authcenter.config.seurity;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
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
                        // .requestMatchers("/test/**").permitAll()
                        .requestMatchers("/login.html", "/css/**", "/js/**", "/images/**", "/favicon.ico").permitAll()
                        .requestMatchers("/custom-login", "/login").permitAll()
                        .requestMatchers("/home").permitAll()
                        .requestMatchers("/test/yltest").permitAll()
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
    RegisteredClientRepository registeredClientRepository() {
        RegisteredClient certificationCatalogClient = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("certification-catalog")
                .clientSecret("{noop}secret")
                .clientName("Certification Catalog")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantTypes(gts -> {
                    gts.add(AuthorizationGrantType.AUTHORIZATION_CODE);
                    gts.add(AuthorizationGrantType.REFRESH_TOKEN);
                    // gts.add(AuthorizationGrantType.CLIENT_CREDENTIALS);
                })
                .redirectUris((uris -> {
                    uris.add("http://127.0.0.1:20000/login/oauth2/code/certification-catalog-oidc");
                    
                }))
                .scopes(s -> {
                    s.add("openid");
                //     s.add("articles.read");
                    // s.add("server");
                })
                .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
                .tokenSettings(TokenSettings.builder().refreshTokenTimeToLive(Duration.ofMinutes(3)).build())
                .build();
        
        RegisteredClient pkceClient = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("pkce-client")
                .clientAuthenticationMethod(ClientAuthenticationMethod.NONE)
                .authorizationGrantTypes(gts -> {
                    gts.add(AuthorizationGrantType.AUTHORIZATION_CODE);
                    gts.add(AuthorizationGrantType.REFRESH_TOKEN);
                })
                .redirectUri("http://vue-front-before-gateway.clouddizai.com:20005/home")
                .postLogoutRedirectUri("http://vue-front-before-gateway.clouddizai.com:20005")
                .scope(OidcScopes.OPENID) //必须添加，获取id令牌
                .scope("catalog.edit")
                .scope("catalog.read")
                .clientSettings(
                        ClientSettings.builder()
                        .requireProofKey(true) // 强制 PKCE
                        .build())
                .tokenSettings(TokenSettings.builder().refreshTokenTimeToLive(Duration.ofMinutes(32))
                                                      .accessTokenTimeToLive(Duration.ofMinutes(32)).build())
                .build();
        
        RegisteredClient gateway = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("gateway")
                .clientSecret("{noop}gatewaysecret")
                .clientName("gateway")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantTypes(gts -> {
                    gts.add(AuthorizationGrantType.AUTHORIZATION_CODE);
                    gts.add(AuthorizationGrantType.REFRESH_TOKEN);
                    gts.add(AuthorizationGrantType.CLIENT_CREDENTIALS);
                })
                .redirectUris((uris -> {
                    uris.add("http://gateway.clouddizai.com:20001/login/oauth2/code/gateway");
                }))
                // .postLogoutRedirectUri("http://127.0.0.1:10000")
                .scopes(s -> {
                    s.add("openid");
                    s.add("articles.read");
                    // s.add("server");
                })
                .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
                .tokenSettings(TokenSettings.builder().refreshTokenTimeToLive(Duration.ofHours(5))
                                                      .accessTokenTimeToLive(Duration.ofHours(5)).build())
                .build();
        List<RegisteredClient> clients = new ArrayList<>();
        clients.add(certificationCatalogClient);
        clients.add(pkceClient);
        clients.add(gateway);

        return new InMemoryRegisteredClientRepository(clients);
    }

}
