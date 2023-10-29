package com.br.notesapp.notesappserver.security.jwt;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;

import java.util.Optional;

public class BearerTokenResolver {

    private final String PREFIX = "Bearer ";

    public Optional<String> getToken(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null || !header.startsWith(PREFIX)) {
            return Optional.empty();
        }
        return Optional.of(header.split(PREFIX)[1]);
    }
}
