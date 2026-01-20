package org.ziranziyuanting.rolemanage.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
        @Bean
        @LoadBalanced
        WebClient.Builder loadBalancedWebClientBuilder() {
                return WebClient.builder();
        }

        @Bean
        WebClient webClient(@LoadBalanced WebClient.Builder builder) {
                return builder
                                .baseUrl("http://account:20006")
                                // .filter(oauth2Filter)
                                .build();
        }

        // private ExchangeFilterFunction logRequest() {
        // return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
        // System.out.println("Request: " + clientRequest.method() + " " +
        // clientRequest.url());
        // return Mono.just(clientRequest);
        // });
        // }
}
