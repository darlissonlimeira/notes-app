package com.br.notesapp.notesappserver.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class BearerAuthenticationFilter extends OncePerRequestFilter {

    private final String TOKEN_PREFIX = "Bearer ";

    private final AuthProvider authProvider;

    public BearerAuthenticationFilter(AuthProvider authProvider) {
        this.authProvider = authProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (bearerToken != null && bearerToken.startsWith(TOKEN_PREFIX)) {
            var token = bearerToken.replace(TOKEN_PREFIX, "");
            authProvider.authenticateUser(token);
        }
        filterChain.doFilter(request, response);
    }
    
}
