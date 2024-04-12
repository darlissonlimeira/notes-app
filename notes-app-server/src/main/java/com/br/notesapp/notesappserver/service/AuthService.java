package com.br.notesapp.notesappserver.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.br.notesapp.notesappserver.dto.auth.AuthTokens;
import com.br.notesapp.notesappserver.dto.auth.LoginRequestDTO;
import com.br.notesapp.notesappserver.dto.auth.UserAuthDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UserAuthDetailsService userAuthDetailsService;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserAuthDetailsService userAuthDetailsService, TokenService tokenService, PasswordEncoder passwordEncoder) {
        this.userAuthDetailsService = userAuthDetailsService;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<AuthTokens> login(LoginRequestDTO request) {
        var user = userAuthDetailsService.loadUserByUsername(request.username());
        if (user.isEmpty()) return Optional.empty();
        var passwordMatches = passwordEncoder.matches(request.password(), user.get().getPassword());
        if (!passwordMatches) return Optional.empty();
        UserAuthDetails userDetails = user.get();
        String accessToken = tokenService.getAccessToken(userDetails);
        String refreshToken = tokenService.getRefreshToken(userDetails);
        return Optional.of(new AuthTokens(accessToken, refreshToken));
    }

    public Optional<String> getAccessToken(String refreshToken) {
        Optional<DecodedJWT> decodedJWT = tokenService.verifyRefreshToken(refreshToken);
        if (decodedJWT.isEmpty()) return Optional.empty();
        var user = userAuthDetailsService.loadUserByUsername(decodedJWT.get().getClaim("username").asString());
        if (user.isEmpty()) return Optional.empty();
        UserAuthDetails userDetails = user.get();
        String accessToken = tokenService.getAccessToken(userDetails);
        return Optional.of(accessToken);
    }
}
