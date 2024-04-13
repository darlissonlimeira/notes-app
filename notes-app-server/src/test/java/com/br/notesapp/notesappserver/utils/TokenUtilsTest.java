package com.br.notesapp.notesappserver.utils;

import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

@SpringBootTest
class TokenUtilsTest {

    @Autowired
    TokenUtils tokenUtils;

    @Test
    void tokenBuilder() {
        // given
        var user = User.builder()
                .username("john")
                .password("123")
                .authorities("EMPLOYEE")
                .build();

        // when
        var tokenBuilder = tokenUtils.tokenBuilder(user);

        // then
        assertInstanceOf(JWTCreator.Builder.class, tokenBuilder);
    }

    @Test
    void verify() {
        // given
        var secret = "test_token_secret";
        var user = User.builder()
                .username("john")
                .password("123")
                .authorities("EMPLOYEE")
                .build();
        var token = tokenUtils.tokenBuilder(user)
                .sign(Algorithm.HMAC256(secret));

        // when
        var tokenPayload = tokenUtils.verify(secret, token);

        //then
        assertEquals("john", tokenPayload.username());
        assertEquals(List.of("EMPLOYEE"), tokenPayload.authorities());
    }
}