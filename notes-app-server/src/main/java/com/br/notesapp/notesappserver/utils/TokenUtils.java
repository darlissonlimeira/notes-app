package com.br.notesapp.notesappserver.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.br.notesapp.notesappserver.dto.auth.TokenPayload;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class TokenUtils {

    @Value("${TOKEN_ISSUER}")

    private String tokenIssuer;

    public JWTCreator.Builder tokenBuilder(UserDetails userDetails) {
        var roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toArray(String[]::new);
        return JWT.create()
                .withIssuer(tokenIssuer)
                .withSubject(userDetails.getUsername())
                .withClaim("username", userDetails.getUsername())
                .withArrayClaim("authorities", roles);
    }

    public TokenPayload verify(String secret, String token) {
        var verifier = JWT.require(Algorithm.HMAC256(secret)).withIssuer(tokenIssuer).build();
        var decoded = verifier.verify(token);
        var username = decoded.getClaim("username").asString();
        var authorities = Arrays.stream(decoded.getClaim("authorities").asArray(String.class)).toList();
        return new TokenPayload(username, authorities);
    }
}
