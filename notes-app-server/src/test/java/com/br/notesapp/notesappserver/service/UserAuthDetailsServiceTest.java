package com.br.notesapp.notesappserver.service;

import com.br.notesapp.notesappserver.MongoTestContainer;
import com.br.notesapp.notesappserver.exception.UserNotFoundException;
import com.br.notesapp.notesappserver.model.UserModel;
import com.br.notesapp.notesappserver.model.UserModelRole;
import com.br.notesapp.notesappserver.repository.UserModelRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserAuthDetailsServiceTest extends MongoTestContainer {

    @MockBean
    UserModelRepository userRepository;

    @Autowired
    UserAuthDetailsService userAuthDetailsService;
    
    @Test
    void shouldThrowUserNotFoundExceptionIfUserNotFound() {
        // given / when
        when(userRepository.findByUsername("mark")).thenReturn(Optional.empty());

        // then
        assertThrowsExactly(UserNotFoundException.class, () -> userAuthDetailsService.loadUserByUsername("mark"));
    }

    @Test
    void shouldReturnAUserDetailsIfUserIsFound() {
        // given
        var userModel = new UserModel();
        userModel.setUsername("john");
        userModel.setPassword("123");
        userModel.setRoles(new HashSet<>() {{
            add(UserModelRole.EMPLOYEE);
        }});
        when(userRepository.findByUsername("john")).thenReturn(Optional.of(userModel));

        // when
        var userDetails = userAuthDetailsService.loadUserByUsername("john");

        // then
        assertEquals("john", userDetails.getUsername());
        assertEquals("123", userDetails.getPassword());
        assertEquals(1, userDetails.getAuthorities().size());
        assertTrue(userDetails.getAuthorities().contains(new SimpleGrantedAuthority("EMPLOYEE")));
    }
}