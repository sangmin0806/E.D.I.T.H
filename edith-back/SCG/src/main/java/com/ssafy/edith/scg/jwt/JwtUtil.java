package com.ssafy.edith.scg.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;


@Slf4j
@Component
public class JwtUtil {

    private final SecretKey secretKey;

    public JwtUtil(@Value("${spring.jwt.secret}") String secret) {
        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    public String getAccessToken(ServerHttpRequest request) {
        return request.getCookies().getFirst("accessToken") != null
                ? request.getCookies().getFirst("accessToken").getValue()
                : null;
    }

    public Claims extractClaims(String token) throws JwtException {
        try {
            return Jwts.parser()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException e) {
            return null;
        }
    }

    public boolean isJwtExpired(String token) {
        Claims claims = extractClaims(token);
        return claims == null || claims.getExpiration().before(new Date());
    }

    public boolean isJwtValid(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus status) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(status);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        log.error(err);

        // JSON 형식의 응답 본문 작성
        String jsonResponse = "{ \"success\": false, \"response\": null, \"error\": \"" + err + "\" }";
        DataBufferFactory dataBufferFactory = response.bufferFactory();
        DataBuffer dataBuffer = dataBufferFactory.wrap(jsonResponse.getBytes());

        return response.writeWith(Mono.just(dataBuffer));
    }

}