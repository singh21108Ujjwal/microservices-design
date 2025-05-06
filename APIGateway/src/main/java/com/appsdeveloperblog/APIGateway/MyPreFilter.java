package com.appsdeveloperblog.APIGateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Set;


@Component
public class MyPreFilter implements GlobalFilter {
    final Logger log = LoggerFactory.getLogger(MyPreFilter.class);
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("My first pre-filter is executed ...");

        String requestPath = exchange.getRequest().getPath().toString();
        log.info("Request path = {}", requestPath);

        // we will fetch header from the request
        HttpHeaders headers = exchange.getRequest().getHeaders();
        // converting all the available headers into set
        Set<String> headerNames = headers.keySet();

        headerNames.forEach((headerName) -> {
            String headerValue = headers.getFirst(headerName);
            log.info("header Name: {} Header value: {}", headerName,headerValue);
        });
        return chain.filter(exchange);
    }
}


