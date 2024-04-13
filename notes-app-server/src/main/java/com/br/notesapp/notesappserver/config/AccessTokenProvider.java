package com.br.notesapp.notesappserver.config;

import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.br.notesapp.notesappserver.dto.auth.TokenPayload;
import com.br.notesapp.notesappserver.exception.InvalidBearerTokenException;
import com.br.notesapp.notesappserver.utils.TokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Component
public class AccessTokenProvider implements TokenProvider {

    final String BEARER_TOKEN_PREFIX = "Bearer ";

    @Value("${ACCESS_TOKEN_SECRET}")
    private String tokenSecret;

    private TokenUtils tokenUtils;

    public AccessTokenProvider(TokenUtils tokenUtils) {
        this.tokenUtils = tokenUtils;
    }

    @Override
    public String resolve(HttpServletRequest request) throws InvalidBearerTokenException {
        var token = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (token == null || !token.startsWith(BEARER_TOKEN_PREFIX)) throw new InvalidBearerTokenException();
        return token.replace(BEARER_TOKEN_PREFIX, "");
    }

    @Override
    public String generate(UserDetails userDetails) {
        var tokenBuilder = tokenUtils.tokenBuilder(userDetails);
        return tokenBuilder.
                withExpiresAt(Instant.now().plus(15, ChronoUnit.MINUTES))
                .sign(Algorithm.HMAC256(tokenSecret));
    }

    @Override
    public TokenPayload verify(String token) throws JWTVerificationException {
        return tokenUtils.verify(tokenSecret, token);
    }
}
