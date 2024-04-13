package com.br.notesapp.notesappserver.config;

import com.br.notesapp.notesappserver.exception.InvalidBearerTokenException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AccessTokenProviderTest {

    @Autowired
    AccessTokenProvider tokenProvider;

    UserDetails user;

    @BeforeEach
    void setUp() {
        user = User.builder().username("john").password("123").authorities("EMPLOYEE").build();
    }

    @Test
    void shouldResolveTokenWithSuccess() {
        // given
        var testToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJPbmxpbmUgSldUIEJ1aWxkZXIiLCJpYXQiOjE3MTMwMjMxMzcsImV4cCI6MTc0NDU1OTEzNywiYXVkIjoid3d3LmV4YW1wbGUuY29tIiwic3ViIjoianJvY2tldEBleGFtcGxlLmNvbSIsIkdpdmVuTmFtZSI6IkpvaG5ueSIsIlN1cm5hbWUiOiJSb2NrZXQiLCJFbWFpbCI6Impyb2NrZXRAZXhhbXBsZS5jb20iLCJSb2xlIjpbIk1hbmFnZXIiLCJQcm9qZWN0IEFkbWluaXN0cmF0b3IiXX0.7hXamaI7rX88DCovCTrm0PFiCNHzB000L3N2FFoA-Dw";
        var request = new MockHttpServletRequest();
        request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + testToken);

        // when
        var token = tokenProvider.resolve(request);

        // then
        assertEquals(testToken, token);
    }

    @Test
    void shouldGenerateTokenWithSuccess() {
        // given / when
        var token = tokenProvider.generate(user);

        // then
        assertFalse(token.isEmpty());
        assertEquals(3, token.split("\\.").length);
    }

    @Test
    void shouldVerifyTokenWithSuccess() {
        // given
        var token = tokenProvider.generate(user);
        var authorites = new String[]{"EMPLOYEE"};

        // when
        var tokenPayload = tokenProvider.verify(token);

        // then
        assertEquals("john", tokenPayload.username());
        assertEquals(List.of("EMPLOYEE"), tokenPayload.authorities());
    }

    @Test
    void shouldThrowInvalidBearerTokenExceptionIfTokenNotExists() {
        // given
        var request = new MockHttpServletRequest();

        // when / then
        assertThrowsExactly(InvalidBearerTokenException.class, () -> tokenProvider.resolve(request));
    }
}