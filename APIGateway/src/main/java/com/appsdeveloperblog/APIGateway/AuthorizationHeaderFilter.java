package com.appsdeveloperblog.APIGateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.util.Base64;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory {
    @Autowired
    Environment env;

    public static class Config{
        // put configuration properties here
    }
    public AuthorizationHeaderFilter() {
        super(Config.class);
    }


    //main business logic of our custom filters and we can customise it.
    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {

            ServerHttpRequest request = exchange.getRequest();
            // if it returns error
            if(!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)){
                return onError(exchange, "No Authorization header", HttpStatus.UNAUTHORIZED);
            }

            // extracting JWT from Authorization header
            String authorizationHeader = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            // removing Bearer from jwt
            String jwt = authorizationHeader.replace("Bearer", "").trim();

            if(!isJwtValid(jwt)){
                return onError(exchange, "JWT token is not valid", HttpStatus.UNAUTHORIZED);
            }

            request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            return  chain.filter(exchange);
        };
    }

    private Mono<Void> onError(ServerWebExchange exchange, String error, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }
    private boolean isJwtValid(String jwt) {
        boolean returnValue = true;

        String subject = null;
        // fetching token secret which was used to sign the JWT in user-ms
        String tokenSecret = env.getProperty("token.secret");
        // generating array of bytes to create a signing key
        byte[] secretKeyBytes = Base64.getEncoder().encode(tokenSecret.getBytes());
        SecretKey secretKey = Keys.hmacShaKeyFor(secretKeyBytes);

        // validating JWT
        JwtParser parser = Jwts.parser()
                .verifyWith(secretKey)
                .build();

        try {
            // parsing claims
            Claims claims = parser.parseSignedClaims(jwt).getPayload();
            // subject will be our userId
            subject = (String) claims.get("sub");

        } catch (Exception ex) {
            // if any exception takes place then we are returning false i.e token validation failed
            returnValue = false;
        }

        // if subject is null or not present then we are returning false i.e token validation failed
        if (subject == null || subject.isEmpty()) {
            returnValue = false;
        }

        return returnValue;
    }
}
