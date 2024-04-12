package com.br.notesapp.notesappserver.service;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;

import java.lang.reflect.InvocationTargetException;

@SpringBootTest
class JWTCookieServiceTest {

    @Autowired
    JWTCookieService service;

    Cookie jwtCookie;

    @BeforeEach
    void setjwtCookie() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        var createJWTCookie = JWTCookieService.class.getDeclaredMethod("createJWTCookie", String.class, int.class);
        createJWTCookie.setAccessible(true);
        jwtCookie = (Cookie) createJWTCookie.invoke(service, "jwt-cookie-value", 5 * 60 * 1000);
    }

    @Test
    @DisplayName("should get a token from request with success")
    void getJWT() {
        // Given
        var request = new MockHttpServletRequest();
        request.setCookies(jwtCookie);

        // When
        var token = service.getJWT(request);

        // Then
        Assertions.assertEquals(token.get(), "jwt-cookie-value");
    }
}