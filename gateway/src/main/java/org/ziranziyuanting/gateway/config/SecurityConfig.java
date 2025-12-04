package org.ziranziyuanting.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
// import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
// import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager;
// import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientProvider;
// import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientProviderBuilder;
// import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
// import org.springframework.security.oauth2.client.web.DefaultReactiveOAuth2AuthorizedClientManager;
// import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.HttpStatusServerEntryPoint;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
        private final CustomJwtAuthenticationConverter customJwtAuthenticationConverter;

        public SecurityConfig(CustomJwtAuthenticationConverter customJwtAuthenticationConverter) {
                this.customJwtAuthenticationConverter = customJwtAuthenticationConverter;
        }

        @Bean
        SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http
        // ,ReactiveClientRegistrationRepository reactiveClientRegistrationRepository
        ) throws Exception {
                http
                                .csrf(csrf -> csrf.disable())
                                .formLogin(fLogin -> fLogin.disable())
                                .httpBasic(hBasic -> hBasic.disable())
                                // .sessionManagement(session ->
                                // session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt
                                                .jwtAuthenticationConverter(customJwtAuthenticationConverter)
                                                )
                                        )
                                .authorizeExchange(exchange -> exchange
                                                .pathMatchers("/favicon.ico").permitAll()
                                                // .pathMatchers("/mytest/**").permitAll()
                                                // .pathMatchers("/catalog/**").permitAll()
                                                .anyExchange().authenticated())
                                // 前后端分离项目，请求后端数据时，不应该返回302，而是应该返回401
                                .exceptionHandling(exceptionHandling -> exceptionHandling
                                                .authenticationEntryPoint(new HttpStatusServerEntryPoint(
                                                                HttpStatus.UNAUTHORIZED)))
                // .oauth2Login(
                // Customizer.withDefaults())
                // .oauth2Client(Customizer.withDefaults())
                ;
                return http.build();

                /**
                 * .csrf(csrf -> csrf.disable())
                 * .sessionManagement(session ->
                 * session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                 * .oauth2ResourceServer(oauth2 -> oauth2.jwt(
                 * // Customizer.withDefaults()
                 * jwt->jwt.jwtAuthenticationConverter(customJwtAuthenticationConverter)
                 * ))
                 * .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                 * // .requestMatchers("/test/**").permitAll()
                 * // .requestMatchers("/user/**").permitAll()
                 * .anyRequest().authenticated()
                 * )
                 */
        }

        // @Bean
        // public ReactiveOAuth2AuthorizedClientManager authorizedClientManager(
        // ReactiveClientRegistrationRepository clientRegistrationRepository,
        // ServerOAuth2AuthorizedClientRepository authorizedClientRepository) {

        // ReactiveOAuth2AuthorizedClientProvider authorizedClientProvider =
        // ReactiveOAuth2AuthorizedClientProviderBuilder
        // .builder()
        // .clientCredentials()
        // .authorizationCode()
        // .build();

        // DefaultReactiveOAuth2AuthorizedClientManager authorizedClientManager = new
        // DefaultReactiveOAuth2AuthorizedClientManager(
        // clientRegistrationRepository, authorizedClientRepository);
        // authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);

        // return authorizedClientManager;
        // }
}
