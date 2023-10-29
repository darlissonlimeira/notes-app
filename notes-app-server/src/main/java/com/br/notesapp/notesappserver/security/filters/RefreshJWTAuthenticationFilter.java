package com.br.notesapp.notesappserver.security.filters;

import com.br.notesapp.notesappserver.security.RefreshJWTAuthenticationToken;
import com.br.notesapp.notesappserver.security.jwt.AuthResponse;
import com.br.notesapp.notesappserver.security.jwt.JWTProvider;
import com.google.gson.Gson;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.firewall.RequestRejectedException;

import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class RefreshJWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final JWTProvider jwtProvider;

    public RefreshJWTAuthenticationFilter(AuthenticationManager authenticationManager, JWTProvider jwtProvider) {
        super(authenticationManager);
        setFilterProcessesUrl("/api/auth/refresh");
        setPostOnly(false);
        this.jwtProvider = jwtProvider;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (!request.getMethod().equals("GET")) {
            throw new RequestRejectedException(String.format("%s request method is not supported", request.getMethod()));
        }

        Optional<String> refreshToken = jwtProvider.extractRefreshToken("jwt", request);
        if (refreshToken.isEmpty()) {
            throw new BadCredentialsException("missing jwt");
        }
        return getAuthenticationManager().authenticate(new RefreshJWTAuthenticationToken(refreshToken.get()));
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String accessToken = getAccessToken(authResult);
        AuthResponse responseProvider = new AuthResponse(response);
        responseProvider.addJWTReponseBody(accessToken);
        responseProvider.successReponse();
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        AuthResponse authResponse = new AuthResponse(response);
        authResponse.failReponse(failed.getMessage());
    }

    private String getAccessToken(Authentication authentication) {
        Gson gson = new Gson();
        Map<String, Object> subject = new HashMap<>();
        subject.put("username", authentication.getName());
        subject.put("roles", authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList());
        String json = gson.toJson(subject);
        return jwtProvider.getAccessToken(json, Instant.now().plusSeconds(10));
    }

}
