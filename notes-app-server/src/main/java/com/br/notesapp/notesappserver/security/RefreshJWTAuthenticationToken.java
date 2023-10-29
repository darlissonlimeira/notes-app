package com.br.notesapp.notesappserver.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.io.Serializable;


public class RefreshJWTAuthenticationToken extends UsernamePasswordAuthenticationToken implements Serializable {

    private final String refreshToken;

    public RefreshJWTAuthenticationToken(String refreshToken) {
        super(null, null);
        this.refreshToken = refreshToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}
