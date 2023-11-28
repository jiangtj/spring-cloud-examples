package com.jiangtj.platform.core;

import com.jiangtj.platform.core.pk.PublicKeyService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class Router {

	@Bean
	public RouterFunction<ServerResponse> pkRoutes(PublicKeyService publicKeyService) {
		return route()
			.GET("/services", serverRequest ->
				ServerResponse.ok().body(publicKeyService.getMicroServiceDatas(),
					new ParameterizedTypeReference<>() {}))
			.GET("/service/{id}/publickey", serverRequest ->
					publicKeyService.getPublicKey(serverRequest.pathVariable("id"))
						.flatMap(result -> ServerResponse.ok().bodyValue(result))
						.switchIfEmpty(ServerResponse.notFound().build()))
			.build();
	}
}