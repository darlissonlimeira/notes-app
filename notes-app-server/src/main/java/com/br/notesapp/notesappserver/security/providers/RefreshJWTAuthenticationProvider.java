package com.br.notesapp.notesappserver.security.providers;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.br.notesapp.notesappserver.security.AccountDetails;
import com.br.notesapp.notesappserver.security.AccountDetailsService;
import com.br.notesapp.notesappserver.security.RefreshJWTAuthenticationToken;
import com.br.notesapp.notesappserver.security.jwt.JWTProvider;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.util.Optional;

public class RefreshJWTAuthenticationProvider implements AuthenticationProvider {

    private final AccountDetailsService accountDetailsService;

    private final JWTProvider jwtProvider;

    public RefreshJWTAuthenticationProvider(AccountDetailsService accountDetailsService, JWTProvider jwtProvider) {
        this.accountDetailsService = accountDetailsService;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        var refreshAuthToken = (RefreshJWTAuthenticationToken) authentication;
        Optional<DecodedJWT> decodedJWT = jwtProvider.verifyRefreshToken(refreshAuthToken.getRefreshToken());
        if (decodedJWT.isEmpty()) {
            throw new BadCredentialsException("Invalid refresh token");
        }
        String username = decodedJWT.get().getSubject();
        AccountDetails user = accountDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(user.getUsername(), null, user.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authenticationType) {
        return authenticationType.isAssignableFrom(RefreshJWTAuthenticationToken.class);
    }
}
