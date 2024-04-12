package com.br.notesapp.notesappserver.config;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.br.notesapp.notesappserver.service.TokenService;
import com.google.gson.Gson;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;

@Component
public class AuthProvider {

    private final TokenService tokenService;

    public AuthProvider(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    public void authenticateUser(String token) {
        Optional<DecodedJWT> decodedJWT = tokenService.verifyAccessToken(token);
        if (decodedJWT.isEmpty()) return;
        var username = decodedJWT.get().getClaim("username").asString();
        var authoritiesJson = decodedJWT.get().getClaim("authorities").asString();
        var authorities = Arrays.stream(new Gson().fromJson(authoritiesJson, String[].class)).map(SimpleGrantedAuthority::new).toList();
        var authToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }

}
