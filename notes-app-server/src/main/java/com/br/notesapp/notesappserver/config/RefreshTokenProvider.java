package com.br.notesapp.notesappserver.config;

import com.auth0.jwt.algorithms.Algorithm;
import com.br.notesapp.notesappserver.dto.auth.TokenPayload;
import com.br.notesapp.notesappserver.exception.InvalidRefreshTokenException;
import com.br.notesapp.notesappserver.utils.TokenUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;

@Component
public class RefreshTokenProvider implements TokenProvider {

    private final String JWT_COOKIE = "jwt";
    private final TokenUtils tokenUtils;
    @Value("${REFRESH_TOKEN_SECRET}")
    private String tokenSecret;

    public RefreshTokenProvider(TokenUtils tokenUtils) {
        this.tokenUtils = tokenUtils;
    }

    @Override
    public String resolve(HttpServletRequest request) throws InvalidRefreshTokenException {
        var cookies = request.getCookies() != null ? request.getCookies() : new Cookie[]{};
        var cookie = Arrays.stream(cookies).filter(c -> c.getName().equals(JWT_COOKIE)).findFirst();
        var jwtCookie = cookie.orElseThrow(InvalidRefreshTokenException::new);
        return jwtCookie.getValue();
    }

    @Override
    public String generate(UserDetails userDetails) {
        var tokenBuilder = tokenUtils.tokenBuilder(userDetails);
        return tokenBuilder.
                withExpiresAt(Instant.now().plus(7, ChronoUnit.DAYS))
                .sign(Algorithm.HMAC256(tokenSecret));
    }

    @Override
    public TokenPayload verify(String token) {
        return tokenUtils.verify(tokenSecret, token);
    }
}
