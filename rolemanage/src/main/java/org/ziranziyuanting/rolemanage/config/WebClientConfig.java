package org.ziranziyuanting.rolemanage.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
        @Bean
        WebClient webClient() {
                return WebClient.builder()
                                .baseUrl("http://127.0.0.1:20006")
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
