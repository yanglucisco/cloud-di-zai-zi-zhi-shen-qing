package org.ziranziyuanting.gateway.config;

import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.BlockRequestHandler;
import reactor.core.publisher.Mono;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;

/**
 * @author <a href="mailto:fangjian0423@gmail.com">Jim</a>
 */
@Configuration
public class MySCGConfiguration {

	@Bean
	public BlockRequestHandler blockRequestHandler() {
		return new BlockRequestHandler() {
			@SuppressWarnings("null")
            @Override
			public Mono<ServerResponse> handleRequest(ServerWebExchange exchange,
					Throwable t) {
                System.out.println("快速失败");
				return ServerResponse.status(444).contentType(MediaType.APPLICATION_JSON)
						.body(fromValue("SCS Sentinel block"));
			}
		};
	}

}
