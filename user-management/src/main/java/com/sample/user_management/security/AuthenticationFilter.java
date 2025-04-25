package com.sample.user_management.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sample.user_management.dto.CreateUserResponseDto;
import com.sample.user_management.dto.LoginRequestDto;
import com.sample.user_management.service.UserService;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import io.jsonwebtoken.Jwts;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final UserService userService;
    private Environment environment; // environment variable is used to read anything from application context

    public AuthenticationFilter(AuthenticationManager authenticationManager,
                                UserService userService,
                                Environment environment) {
        super(authenticationManager);
        this.userService = userService;
        this.environment = environment;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
            throws AuthenticationException {
        try {

            LoginRequestDto creds = new ObjectMapper().readValue(req.getInputStream(), LoginRequestDto.class);

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(creds.getEmail(), creds.getPassword(), new ArrayList<>()));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

//    when the authentication is successful then the application will generate this JWT
//    and add it into request header
    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res, FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {
        String userName = ((User) auth.getPrincipal()).getUsername();
        CreateUserResponseDto createUserResponseDto = userService.getUserDetailsByEmail(userName);
        Instant now = Instant.now();
        String secretToken = environment.getProperty("secret.token");
        byte[] secretTokenByte = Base64.getEncoder().encode(secretToken.getBytes());
        SecretKey secretKey = Keys.hmacShaKeyFor(secretTokenByte);

        // creating a JWT token
        String jwtToken = Jwts.builder()
                .subject(createUserResponseDto.getUserId())
                .expiration(Date.from(now
                        .plusMillis(Long.parseLong(environment.getProperty("token.expiration-time")))))
                .issuedAt(Date.from(now))
                .signWith(secretKey)
                .compact();;

        // adding JWT in response header
        res.addHeader("token", jwtToken);
        res.addHeader("userId", createUserResponseDto.getUserId());
    }
}
