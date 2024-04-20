package com.br.notesapp.notesappserver.service;

import com.br.notesapp.notesappserver.config.AccessTokenProvider;
import com.br.notesapp.notesappserver.config.RefreshTokenProvider;
import com.br.notesapp.notesappserver.dto.auth.AuthTokens;
import com.br.notesapp.notesappserver.dto.auth.LoginRequestDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AccessTokenProvider accessTokenProvider;
    private final RefreshTokenProvider refreshTokenProvider;
    private final UserAuthDetailsService userAuthDetailsService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(AccessTokenProvider accessTokenProvider, RefreshTokenProvider refreshTokenProvider, UserAuthDetailsService userAuthDetailsService, PasswordEncoder passwordEncoder) {
        this.accessTokenProvider = accessTokenProvider;
        this.refreshTokenProvider = refreshTokenProvider;
        this.userAuthDetailsService = userAuthDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthTokens login(LoginRequestDTO request) throws BadCredentialsException {
        var userDetails = userAuthDetailsService.loadUserByUsername(request.username());
        if (!passwordEncoder.matches(request.password(), userDetails.getPassword()))
            throw new BadCredentialsException("The provided password do not match the encoded password.");
        var accessToken = accessTokenProvider.generate(userDetails);
        var refreshToken = refreshTokenProvider.generate(userDetails);
        return new AuthTokens(accessToken, refreshToken);
    }

    public String getAccessToken(HttpServletRequest request) {
        var refreshToken = refreshTokenProvider.resolve(request);
        var tokenPayload = refreshTokenProvider.verify(refreshToken);
        var authorities = tokenPayload.authorities().stream().map(SimpleGrantedAuthority::new).toList();
        var user = User.builder().username(tokenPayload.username()).password("").authorities(authorities).build();
        return accessTokenProvider.generate(user);
    }
}
