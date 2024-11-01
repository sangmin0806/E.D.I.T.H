package com.ssafy.edith.scg.filter;


import com.ssafy.edith.scg.jwt.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JwtAuthFilter extends AbstractGatewayFilterFactory<JwtAuthFilter.Config> {

    private final JwtUtil jwtUtil;

    public JwtAuthFilter(JwtUtil jwtUtil) {
        super(Config.class);
        this.jwtUtil = jwtUtil;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String accessToken = jwtUtil.getAccessToken(request);

            if (accessToken != null) {
                if (jwtUtil.isJwtValid(accessToken) && !jwtUtil.isJwtExpired(accessToken)) {
                    return chain.filter(exchange);
                } else if (jwtUtil.isJwtExpired(accessToken)) {
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    exchange.getResponse().getHeaders().add("Token-Status", "expired");
                    return exchange.getResponse().setComplete();
                }
                else{
                    return jwtUtil.onError(exchange, "Invalid JWT Token", HttpStatus.UNAUTHORIZED);
                }
            }

            return jwtUtil.onError(exchange,"JWT Token is missing", HttpStatus.UNAUTHORIZED);
        };
    }

    public static class Config {
    }
}