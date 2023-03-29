package com.jtj.cloud.auth.reactive;

import com.jtj.cloud.auth.AuthExceptionUtils;
import com.jtj.cloud.common.reactive.BaseExceptionHandler;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

@Slf4j
@Order(BaseExceptionHandler.ORDER - 10)
public class ReactiveJWTExceptionHandler implements WebExceptionHandler {

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        if (ex instanceof JwtException) {
            return Mono.error(AuthExceptionUtils.invalidToken(ex.getMessage(), ex));
        }
        return Mono.error(ex);
    }
}