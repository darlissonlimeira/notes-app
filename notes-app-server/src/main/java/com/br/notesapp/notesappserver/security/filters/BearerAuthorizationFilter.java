package com.br.notesapp.notesappserver.security.filters;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.br.notesapp.notesappserver.security.jwt.BearerTokenResolver;
import com.br.notesapp.notesappserver.security.jwt.JWTProvider;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class BearerAuthorizationFilter extends BasicAuthenticationFilter {

    private final BearerTokenResolver tokenResolver;

    private final JWTProvider jwtProvider;


    public BearerAuthorizationFilter(BearerTokenResolver tokenResolver, JWTProvider jwtProvider, AuthenticationManager authenticationManager) {
        super(authenticationManager);
        this.tokenResolver = tokenResolver;
        this.jwtProvider = jwtProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        Optional<String> token = tokenResolver.getToken(request);
        if (token.isEmpty()) {
            return;
        }
        Optional<DecodedJWT> decodedJWT = jwtProvider.verifyAccessToken(token.get());
        if (decodedJWT.isEmpty()) {
            return;
        }
        String subject = decodedJWT.get().getSubject();
        Gson gson = new Gson();
        Type subjectType = new TypeToken<Map<String, Object>>() {
        }.getType();
        Map<String, Object> subjectMap = gson.fromJson(subject, subjectType);
        ArrayList<?> roles = (ArrayList<?>) subjectMap.get("roles");
        List<GrantedAuthority> authorities = roles.stream().map((role) -> new SimpleGrantedAuthority((String) role)).collect(Collectors.toList());
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(subjectMap.get("username"), "", authorities);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }
}
