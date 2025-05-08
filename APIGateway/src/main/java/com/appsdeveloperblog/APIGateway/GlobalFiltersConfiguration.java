package com.appsdeveloperblog.APIGateway;


import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GlobalFiltersConfiguration {

    public GlobalFilter secondFilter(){
        return ((exchange, chain) -> {
            return chain.filter(exchange);
        });
    }
}
