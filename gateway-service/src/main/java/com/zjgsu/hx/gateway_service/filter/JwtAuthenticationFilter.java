package com.zjgsu.hx.gateway_service.filter;

import com.zjgsu.hx.gateway_service.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.jsonwebtoken.Claims;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtUtil jwtUtil;

    private final List<String> whiteList = List.of(
            "/api/user/login",
            "/api/user/register"
    );

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return chain.filter(exchange);
/*
        String path = exchange.getRequest().getURI().getPath();
        logger.info("[JwtFilter] 请求路径: {}", path);

        // 白名单
        if (whiteList.contains(path)) {
            logger.info("[JwtFilter] 白名单路径，直接放行: {}", path);
            return chain.filter(exchange);
        }

        String token = exchange.getRequest()
                .getHeaders()
                .getFirst(HttpHeaders.AUTHORIZATION);

        if (token == null || !token.startsWith("Bearer ")) {
            logger.warn("[JwtFilter] 未携带有效 Authorization Token");
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        token = token.substring(7);
        logger.info("[JwtFilter] 收到 Token: {}", token);

        // 校验 Token
        if (!jwtUtil.validateToken(token)) {
            logger.warn("[JwtFilter] Token 校验失败");
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        Claims claims;
        try {
            claims = jwtUtil.parseToken(token);
            logger.info("[JwtFilter] Token 解析成功, username={}, role={}", claims.getSubject(), claims.get("role"));
        } catch (Exception e) {
            logger.error("[JwtFilter] Token 解析异常", e);
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        // 取出用户信息
        String username = claims.getSubject();
        String role = claims.get("role", String.class);

        // 透传请求头
        ServerHttpRequest mutated = exchange.getRequest().mutate()
                .header("X-USER", username)
                .header("X-ROLE", role)
                .build();

        logger.info("[JwtFilter] 请求头透传完成: X-USER={}, X-ROLE={}", username, role);

        return chain.filter(exchange.mutate().request(mutated).build());

 */
    }

    @Override
    public int getOrder() {
        return -100;
    }
}
