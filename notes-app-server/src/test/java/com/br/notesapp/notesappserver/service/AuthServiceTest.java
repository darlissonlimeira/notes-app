package com.br.notesapp.notesappserver.service;

import com.br.notesapp.notesappserver.config.AccessTokenProvider;
import com.br.notesapp.notesappserver.config.RefreshTokenProvider;
import com.br.notesapp.notesappserver.dto.auth.LoginRequestDTO;
import com.br.notesapp.notesappserver.dto.auth.TokenPayload;
import com.br.notesapp.notesappserver.model.UserModel;
import com.br.notesapp.notesappserver.model.UserModelRole;
import com.br.notesapp.notesappserver.repository.UserModelRepository;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AuthServiceTest {

    UserDetails userDetails;
    UserModel userModel;
    UserModelRepository mockUserRepository;
    PasswordEncoder mockPasswordEncoder;

    @Autowired
    AccessTokenProvider accessTokenProvider;
    @Autowired
    RefreshTokenProvider refreshTokenProvider;

    @BeforeEach
    void setUp() {
        userDetails = User.builder().username("john").password("123").authorities("EMPLOYEE").build();

        userModel = new UserModel();
        userModel.setUsername("john");
        userModel.setPassword("123");
        userModel.setRoles(new HashSet<>() {{
            add(UserModelRole.EMPLOYEE);
        }});

        mockUserRepository = Mockito.mock(UserModelRepository.class);
        mockPasswordEncoder = Mockito.mock(PasswordEncoder.class);
    }

    @Test
    void shouldReturnTokensIfCredentialsAreValid() {
        // given
        Mockito.when(mockUserRepository.findByUsername("john")).thenReturn(Optional.of(userModel));
        Mockito.when(mockPasswordEncoder.matches("123", "123")).thenReturn(true);
        UserAuthDetailsService userAuthDetailsService = new UserAuthDetailsService(mockUserRepository);
        var sut = new AuthService(accessTokenProvider, refreshTokenProvider, userAuthDetailsService, mockPasswordEncoder);

        // when
        var tokens = sut.login(new LoginRequestDTO("john", "123"));

        // then
        assertEquals(3, tokens.accessToken().split("\\.").length);
        assertInstanceOf(TokenPayload.class, accessTokenProvider.verify(tokens.accessToken()));
        assertEquals(3, tokens.refreshToken().split("\\.").length);
        assertInstanceOf(TokenPayload.class, refreshTokenProvider.verify(tokens.refreshToken()));
    }

    @Test
    void shouldThrowBadCredentialsExpectionIfLoginWithWrongCredentials() {
        // given
        Mockito.when(mockUserRepository.findByUsername("john")).thenReturn(Optional.of(userModel));
        Mockito.when(mockPasswordEncoder.matches("12", "123")).thenReturn(false);
        UserAuthDetailsService userAuthDetailsService = new UserAuthDetailsService(mockUserRepository);
        var sut = new AuthService(accessTokenProvider, refreshTokenProvider, userAuthDetailsService, mockPasswordEncoder);

        // when
        // then
        assertThrowsExactly(BadCredentialsException.class, () -> sut.login(new LoginRequestDTO("john", "12")));
    }

    @Test
    void shouldReturnAccessTokenWithSuccessIfGivenAValidRefreshToken() {
        // given
        var refreshToken = refreshTokenProvider.generate(userDetails);
        var request = new MockHttpServletRequest();
        var cookie = new Cookie("jwt", refreshToken);
        request.setCookies(cookie);
        UserAuthDetailsService userAuthDetailsService = new UserAuthDetailsService(mockUserRepository);
        var sut = new AuthService(accessTokenProvider, refreshTokenProvider, userAuthDetailsService, mockPasswordEncoder);

        // when
        var accessToken = sut.getAccessToken(request);

        // then
        assertEquals(3, accessToken.split("\\.").length);
    }

}