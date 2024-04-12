package com.br.notesapp.notesappserver.service;


import com.auth0.jwt.interfaces.DecodedJWT;
import com.br.notesapp.notesappserver.dto.auth.UserAuthDetails;
import com.br.notesapp.notesappserver.model.UserModel;
import com.br.notesapp.notesappserver.model.UserModelRole;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

@SpringBootTest
public class TokenServiceTests {

    @Autowired
    public TokenService service;
    private UserAuthDetails userAuthDetails;


    @BeforeEach
    void initAuthentication() {
        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("Admin"));
        var model = new UserModel();
        model.setUsername("john.doe");
        model.setPassword("123");
        model.getRoles().add(UserModelRole.ADMIN);
        userAuthDetails = new UserAuthDetails(model);
    }

    @Test
    @DisplayName("Should return a valid jwt access token")
    void shouldGenerateValidAccessToken() {
        var token = service.getAccessToken(userAuthDetails);
        var decodedJWT = service.verifyAccessToken(token);

        Assertions.assertInstanceOf(DecodedJWT.class, decodedJWT.get());
        Assertions.assertEquals(decodedJWT.get().getIssuer(), "NotesApp");
        Assertions.assertEquals(decodedJWT.get().getClaim("username").asString(), "john.doe");
        Assertions.assertEquals(decodedJWT.get().getClaim("authorities").asString(), "[ADMIN]");
        Assertions.assertTrue(decodedJWT.get().getExpiresAt().before(Date.from(Instant.now().plus(7, ChronoUnit.DAYS))));
    }

    @Test
    @DisplayName("Should return a valid jwt refresh token")
    void shouldGenerateValidRefreshToken() {
        var token = service.getRefreshToken(userAuthDetails);
        var decodedJWT = service.verifyRefreshToken(token);
        Assertions.assertInstanceOf(DecodedJWT.class, decodedJWT.get());
        Assertions.assertEquals(decodedJWT.get().getIssuer(), "NotesApp");
        Assertions.assertEquals(decodedJWT.get().getClaim("username").asString(), "john.doe");
        Assertions.assertEquals(decodedJWT.get().getClaim("authorities").asString(), "[ADMIN]");
        Assertions.assertTrue(decodedJWT.get().getExpiresAt().before(Date.from(Instant.now().plus(30, ChronoUnit.DAYS))));
    }
}
