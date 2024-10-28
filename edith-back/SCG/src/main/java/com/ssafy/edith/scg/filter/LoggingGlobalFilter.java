package com.ssafy.edith.scg.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Order(-1)
@Slf4j
public class LoggingGlobalFilter implements GlobalFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, org.springframework.cloud.gateway.filter.GatewayFilterChain chain) {
        String requestUri = exchange.getRequest().getURI().toString();
        log.info("Routing request to URI: {}", requestUri);
        exchange.getRequest().getCookies().forEach((name, cookies) -> {
            for (HttpCookie cookie : cookies) {
                log.info("Cookie Name: {}, Cookie Value: {}", cookie.getName(), cookie.getValue());
            }
        });
        return chain.filter(exchange);
    }
}