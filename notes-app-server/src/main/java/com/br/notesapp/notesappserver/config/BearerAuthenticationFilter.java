package com.br.notesapp.notesappserver.config;

import com.br.notesapp.notesappserver.exception.InvalidBearerTokenException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class BearerAuthenticationFilter extends OncePerRequestFilter {

    private final BearerAuthenticationProvider bearerAuthenticationProvider;

    public BearerAuthenticationFilter(BearerAuthenticationProvider bearerAuthenticationProvider) {
        this.bearerAuthenticationProvider = bearerAuthenticationProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            var authentication = bearerAuthenticationProvider.authenticateUser(request);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (InvalidBearerTokenException ex) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
        }
        filterChain.doFilter(request, response);
    }

}
