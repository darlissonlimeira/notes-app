package com.br.notesapp.notesappserver.security.filters;

import com.br.notesapp.notesappserver.dto.LoginRequestDTO;
import com.br.notesapp.notesappserver.security.jwt.AuthResponse;
import com.br.notesapp.notesappserver.security.jwt.JWTProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final Gson gson = new Gson();
    private final JWTProvider jwtProvider;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, JWTProvider jwtProvider) {
        super.setFilterProcessesUrl("/api/auth");
        setAuthenticationManager(authenticationManager);
        this.jwtProvider = jwtProvider;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            LoginRequestDTO loginRequest = new ObjectMapper().readValue(request.getInputStream(), LoginRequestDTO.class);
            var authToken = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
            return getAuthenticationManager().authenticate(authToken);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String accessToken = getAccessToken(authResult);
        String refreshToken = getRefreshToken(authResult);
        AuthResponse reponseProvider = new AuthResponse(response);
        reponseProvider.addJWTCookie(refreshToken);
        reponseProvider.addJWTReponseBody(accessToken);
        reponseProvider.successReponse();
    }

    private String getAccessToken(Authentication user) {
        Map<String, Object> accessSubject = new HashMap<>();
        accessSubject.put("username", user.getName());
        accessSubject.put("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList());
        String subjectJson = gson.toJson(accessSubject);
        return jwtProvider.getAccessToken(subjectJson, Instant.now().plus(1, ChronoUnit.DAYS));
    }

    private String getRefreshToken(Authentication user) {
        return jwtProvider.getRefreshToken(user.getName(), Instant.now().plus(5, ChronoUnit.DAYS));
    }
}
