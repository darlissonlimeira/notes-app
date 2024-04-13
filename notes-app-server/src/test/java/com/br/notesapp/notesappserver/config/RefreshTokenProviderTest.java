package com.br.notesapp.notesappserver.config;

import com.br.notesapp.notesappserver.exception.InvalidRefreshTokenException;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RefreshTokenProviderTest {

    @Autowired
    RefreshTokenProvider tokenProvider;

    UserDetails user;

    @BeforeEach
    void createUser() {
        user = User.builder().username("john").password("123").authorities("EMPLOYEE").build();
    }


    @Test
    void shouldGenerateTokenWithSuccess() {
        var token = tokenProvider.generate(user);

        assertFalse(token.isEmpty());
        assertEquals(3, token.split("\\.").length);
    }

    @Test
    void shouldVerifyTokenWithSuccess() {
        // given
        var token = tokenProvider.generate(user);
        var authorities = new String[]{"EMPLOYEE"};

        // when
        var tokenPayload = tokenProvider.verify(token);

        // then
        assertEquals("john", tokenPayload.username());
        assertEquals(List.of("EMPLOYEE"), tokenPayload.authorities());
    }

    @Test
    void shouldResolveTokenWithSuccess() {
        // given
        var refresToken = tokenProvider.generate(user);
        var cookie = new Cookie("jwt", refresToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(10 * 60 * 1000);
        cookie.setAttribute("SameSite", "None");
        var request = new MockHttpServletRequest();
        request.setCookies(cookie);

        // when
        var token = tokenProvider.resolve(request);

        // then
        assertFalse(token.isEmpty());
        assertEquals(3, token.split("\\.").length);
    }

    @Test
    void shouldThrowInvalidRefreshTokenExceptionIfJWTCookieNotExists() {
        // given
        var request = new MockHttpServletRequest();

        // when / then
        assertThrowsExactly(InvalidRefreshTokenException.class, () -> tokenProvider.resolve(request));
    }

}