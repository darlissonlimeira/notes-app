package com.br.notesapp.notesappserver.service;

import com.br.notesapp.notesappserver.MongoTestContainer;
import com.br.notesapp.notesappserver.config.AccessTokenProvider;
import com.br.notesapp.notesappserver.config.RefreshTokenProvider;
import com.br.notesapp.notesappserver.dto.auth.LoginRequestDTO;
import com.br.notesapp.notesappserver.dto.auth.TokenPayload;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AuthServiceTest extends MongoTestContainer {

    UserDetails userDetails;

    @Autowired
    AccessTokenProvider accessTokenProvider;

    @Autowired
    RefreshTokenProvider refreshTokenProvider;

    @MockBean
    PasswordEncoder mockPasswordEncoder;

    @MockBean
    UserAuthDetailsService userAuthDetailsService;

    @Autowired
    AuthService authService;

    @BeforeEach
    void setUp() {
        userDetails = User.builder().username("john").password("123").authorities("EMPLOYEE").build();
    }

    @Test
        // givenCorrectUserCredentials_WhenLogin_ThenReturnAccessAndRefreshTokens
    void shouldReturnTokensIfValidCredentials() {

        Mockito.when(userAuthDetailsService.loadUserByUsername("john")).thenReturn(userDetails);
        Mockito.when(mockPasswordEncoder.matches("123", "123")).thenReturn(true);

        var tokens = authService.login(new LoginRequestDTO("john", "123"));

        assertEquals(3, tokens.accessToken().split("\\.").length);
        assertInstanceOf(TokenPayload.class, accessTokenProvider.verify(tokens.accessToken()));
        assertEquals(3, tokens.refreshToken().split("\\.").length);
        assertInstanceOf(TokenPayload.class, refreshTokenProvider.verify(tokens.refreshToken()));
    }

    @Test
        // givenUserWrongCredentials_WhenLogin_ThenThrowBadCredentialsException
    void shouldThrowBadCredentialsExpectionIfLoginWithWrongCredentials() {

        Mockito.when(userAuthDetailsService.loadUserByUsername("john")).thenReturn(userDetails);
        Mockito.when(mockPasswordEncoder.matches("12", "123")).thenReturn(false);

        assertThrowsExactly(BadCredentialsException.class, () -> authService.login(new LoginRequestDTO("john", "12")));
    }

    @Test
        // givenRequestWithJWTCookie_WhenGetRefreshToken_ThenReturnAccessToken
    void shouldReturnAccessTokenWithSuccessIfGivenAValidRefreshToken() {

        var refreshToken = refreshTokenProvider.generate(userDetails);
        var request = new MockHttpServletRequest();
        var cookie = new Cookie("jwt", refreshToken);
        request.setCookies(cookie);

        var accessToken = authService.getAccessToken(request);

        assertEquals(3, accessToken.split("\\.").length);
    }

}