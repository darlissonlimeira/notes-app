package com.br.notesapp.notesappserver.config;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.br.notesapp.notesappserver.exception.InvalidBearerTokenException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class BearerAuthenticationProvider {

    private final AccessTokenProvider accessTokenProvider;

    public BearerAuthenticationProvider(AccessTokenProvider accessTokenProvider) {
        this.accessTokenProvider = accessTokenProvider;
    }

    public Authentication authenticateUser(HttpServletRequest request) throws InvalidBearerTokenException, JWTVerificationException {
        var token = accessTokenProvider.resolve(request);
        var tokenPayload = accessTokenProvider.verify(token);
        var username = tokenPayload.username();
        var roles = tokenPayload.authorities().stream().map(SimpleGrantedAuthority::new).toList();
        return new UsernamePasswordAuthenticationToken(username, null, roles);
    }

}
