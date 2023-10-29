package com.br.notesapp.notesappserver.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import java.time.Instant;
import java.util.Arrays;
import java.util.Optional;

public class JWTProvider {

    private final String ACCESS_TOKEN_SECRET;

    private final String REFRESH_TOKEN_SECRET;

    public JWTProvider(String accessTokenSecret, String refreshTokenSecret) {
        this.ACCESS_TOKEN_SECRET = accessTokenSecret;
        this.REFRESH_TOKEN_SECRET = refreshTokenSecret;
    }

    public String getAccessToken(String subject, Instant expiresAt) {
        return JWT.create()
                .withSubject(subject)
                .withExpiresAt(expiresAt)
                .sign(Algorithm.HMAC384(ACCESS_TOKEN_SECRET));
    }

    public String getRefreshToken(String subject, Instant expiresAt) {
        return JWT.create()
                .withSubject(subject)
                .withExpiresAt(expiresAt)
                .sign(Algorithm.HMAC384(REFRESH_TOKEN_SECRET));
    }

    public Optional<DecodedJWT> verifyAccessToken(String token) {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC384(ACCESS_TOKEN_SECRET))
                .build();
        try {
            DecodedJWT verified = verifier.verify(token);
            return Optional.of(verified);
        } catch (RuntimeException exception) {
            System.out.println(exception.getMessage());
            return Optional.empty();
        }
    }

    public Optional<DecodedJWT> verifyRefreshToken(String token) {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC384(REFRESH_TOKEN_SECRET))
                .build();
        try {
            DecodedJWT verified = verifier.verify(token);
            return Optional.of(verified);
        } catch (RuntimeException exception) {
            return Optional.empty();
        }
    }

    public Optional<String> extractRefreshToken(String cookiceName, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        Optional<Cookie> jwtCookie = Arrays.stream(cookies).filter(cookie -> cookie.getName().equals(cookiceName)).findFirst();
        if (jwtCookie.isEmpty()) return Optional.empty();

        String refreshToken = jwtCookie.get().getValue();
        if (refreshToken.isEmpty()) return Optional.empty();
        
        return Optional.of(refreshToken);
    }

}
