package com.br.notesapp.notesappserver.service;

import com.br.notesapp.notesappserver.exception.UserNotFoundException;
import com.br.notesapp.notesappserver.model.UserModel;
import com.br.notesapp.notesappserver.model.UserModelRole;
import com.br.notesapp.notesappserver.repository.UserModelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UserAuthDetailsServiceTest {

    UserModelRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserModelRepository.class);
    }

    @Test
    void shouldThrowUserNotFoundExceptionIfUserNotFound() {
        // given / when
        Mockito.when(userRepository.findByUsername("mark")).thenReturn(Optional.empty());
        var sut = new UserAuthDetailsService(userRepository);

        // then
        assertThrowsExactly(UserNotFoundException.class, () -> sut.loadUserByUsername("mark"));
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
        Mockito.when(userRepository.findByUsername("john")).thenReturn(Optional.of(userModel));
        var sut = new UserAuthDetailsService(userRepository);

        // when
        var userDetails = sut.loadUserByUsername("john");

        // then
        assertEquals("john", userDetails.getUsername());
        assertEquals("123", userDetails.getPassword());
        assertEquals(1, userDetails.getAuthorities().size());
        assertTrue(userDetails.getAuthorities().contains(new SimpleGrantedAuthority("EMPLOYEE")));
    }
}