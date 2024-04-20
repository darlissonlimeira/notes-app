package com.br.notesapp.notesappserver.config;

import com.br.notesapp.notesappserver.MongoTestContainer;
import com.br.notesapp.notesappserver.dto.auth.TokenPayload;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

@SpringBootTest
class BearerAuthenticationProviderTest extends MongoTestContainer {

    @MockBean
    AccessTokenProvider tokenProvider;

    @Autowired
    BearerAuthenticationProvider bearerAuthenticationProvider;

    @Test
    void shouldAuthenticateUserWithSuccess() {
        // given
        var request = new MockHttpServletRequest();
        Mockito.when(tokenProvider.resolve(request)).thenReturn("access_token");
        Mockito.when(tokenProvider.verify("access_token")).thenReturn(new TokenPayload("john", List.of("EMPLOYEE")));

        // when
        var authentication = bearerAuthenticationProvider.authenticateUser(request);

        // then
        Assertions.assertEquals("john", authentication.getName());
        Assertions.assertEquals(List.of(new SimpleGrantedAuthority("EMPLOYEE")), authentication.getAuthorities());

    }
}