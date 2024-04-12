package com.br.notesapp.notesappserver.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.Verification;
import com.br.notesapp.notesappserver.dto.auth.UserAuthDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
public class TokenService {

    private final String TOKEN_ISSUER = "NotesApp";

    @Value("${REFRESH_TOKEN_SECRET}")
    private String REFRESH_TOKEN_SECRET;

    @Value("${ACCESS_TOKEN_SECRET}")
    private String ACCESS_TOKEN_SECRET;

    public TokenService() {
    }

    public String getAccessToken(UserAuthDetails userDetails) {
        return JWTBuilder(userDetails)
                .withExpiresAt(Instant.now().plus(15, ChronoUnit.MINUTES))
                .sign(Algorithm.HMAC256(ACCESS_TOKEN_SECRET));
    }

    public String getRefreshToken(UserAuthDetails userDetails) {
        return JWTBuilder(userDetails)
                .withExpiresAt(Instant.now().plus(7, ChronoUnit.DAYS))
                .sign(Algorithm.HMAC256(REFRESH_TOKEN_SECRET));
    }

    public Optional<DecodedJWT> verifyAccessToken(String token) {
        return verifyToken(ACCESS_TOKEN_SECRET, token);
    }

    public Optional<DecodedJWT> verifyRefreshToken(String token) {
        return verifyToken(REFRESH_TOKEN_SECRET, token);
    }

    private JWTCreator.Builder JWTBuilder(UserAuthDetails userDetails) {
        String username = userDetails.getUsername();
        return JWT.create()
                .withIssuer(TOKEN_ISSUER)
                .withSubject(username)
                .withClaim("username", username)
                .withClaim("authorities", userDetails.getAuthorities().toString());
    }

    private Optional<DecodedJWT> verifyToken(String secret, String token) {
        Verification verification = JWT.require(Algorithm.HMAC256(secret));
        JWTVerifier verifier = verification.withIssuer(TOKEN_ISSUER).build();
        try {
            return Optional.of(verifier.verify(token));
        } catch (JWTVerificationException exception) {
            return Optional.empty();
        }
    }
}
