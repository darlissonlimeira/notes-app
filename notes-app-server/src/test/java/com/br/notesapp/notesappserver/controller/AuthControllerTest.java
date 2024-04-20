package com.br.notesapp.notesappserver.controller;

import com.auth0.jwt.algorithms.Algorithm;
import com.br.notesapp.notesappserver.MongoTestContainer;
import com.br.notesapp.notesappserver.config.RefreshTokenProvider;
import com.br.notesapp.notesappserver.dto.auth.LoginRequestDTO;
import com.br.notesapp.notesappserver.utils.TokenUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest extends MongoTestContainer {

    @Autowired
    MockMvc mockMvc;

    ObjectMapper mapper;

    @Autowired
    private TokenUtils tokenUtils;

    @Value("${REFRESH_TOKEN_SECRET}")
    private String tokenSecret;

    @Autowired
    private RefreshTokenProvider refreshTokenProvider;

    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper();
    }

    @Test
    void shouldRespondWithAccessAndRefreshTokensIfLoginWithCorrectUserUsernameAndPassword() throws Exception {
        var requestJson = mapper.writeValueAsBytes(new LoginRequestDTO("Mark", "123"));
        mockMvc.perform(post("/api/auth/login")
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.access_token").exists())
                .andExpect(cookie().exists("jwt"));

    }

    @Test
    void shouldRespondWithStatus401IfLoginWithIncorrectUserCredentials() throws Exception {
        var requestJson = mapper.writeValueAsBytes(new LoginRequestDTO("Mark", "1234"));
        mockMvc.perform(post("/api/auth/login")
                .content(requestJson)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isUnauthorized());
    }

    @Test
    void shouldRespondWithStatus401IfLoginWithAUserThatNotExists() throws Exception {
        var requestJson = mapper.writeValueAsBytes(new LoginRequestDTO("Mary", "123"));
        mockMvc.perform(post("/api/auth/login")
                .content(requestJson)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isUnauthorized());
    }

    @Test
    void shouldRespondWithAccessTokenIfRefreshAuthenticationWithValidRefreshToken() throws Exception {
        UserDetails user = User.builder().username("john").password("123").authorities("EMPLOYEE").build();
        String token = refreshTokenProvider.generate(user);
        Cookie cookie = new Cookie("jwt", token);

        mockMvc.perform(get("/api/auth/refresh").cookie(cookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.access_token").exists());
    }

    @Test
    void shouldRespondWithStatus403IfRefreshAuthenticationWithExpiredRefreshToken() throws Exception {
        UserDetails user = User.builder().username("john").password("123").authorities("EMPLOYEE").build();
        var token = tokenUtils.tokenBuilder(user).withExpiresAt(Instant.now().minus(1, ChronoUnit.MINUTES)).sign(Algorithm.HMAC256(tokenSecret));
        Cookie cookie = new Cookie("jwt", token);

        mockMvc.perform(get("/api/auth/refresh").cookie(cookie))
                .andExpect(status().isForbidden());
    }
}