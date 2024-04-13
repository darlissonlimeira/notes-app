package com.br.notesapp.notesappserver.config;

import com.br.notesapp.notesappserver.dto.auth.TokenPayload;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

@SpringBootTest
class BearerAuthenticationProviderTest {


    AccessTokenProvider tokenProvider;

    UserDetails user;

    @BeforeEach
    void setUp() {
        user = User.builder().username("john").password("123").authorities("EMPLOYEE").build();
        tokenProvider = Mockito.mock(AccessTokenProvider.class);
    }

    @Test
    void shouldAuthenticateUserWithSuccess() {
        // given
        var request = new MockHttpServletRequest();
        Mockito.when(tokenProvider.resolve(request)).thenReturn("access_token");
        Mockito.when(tokenProvider.verify("access_token")).thenReturn(new TokenPayload("john", List.of("EMPLOYEE")));
        var authenticationProvider = new BearerAuthenticationProvider(tokenProvider);

        // when
        var authentication = authenticationProvider.authenticateUser(request);

        Assertions.assertEquals("john", authentication.getName());
        Assertions.assertEquals(List.of(new SimpleGrantedAuthority("EMPLOYEE")), authentication.getAuthorities());

    }
}