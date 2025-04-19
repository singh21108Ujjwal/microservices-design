package com.sample.user_management.security;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class WebSecurity {

    private Environment environment;

    @Bean
    protected SecurityFilterChain configure(HttpSecurity httpSecurity) throws Exception{
        httpSecurity.csrf((csrf) -> csrf.disable());

        httpSecurity.authorizeHttpRequests((autz) -> autz
                .requestMatchers(new AntPathRequestMatcher("/user")).access(
                        new WebExpressionAuthorizationManager("hasIpAddress('" + environment.getProperty("gateway-ip") + "')")
                )
                .requestMatchers(new AntPathRequestMatcher("/h2-console/**")).permitAll())
                .sessionManagement((session)
                        -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        httpSecurity.headers((headers) -> headers.frameOptions((frameOptions)
                -> frameOptions.sameOrigin()));

        return httpSecurity.build();

    }
}
