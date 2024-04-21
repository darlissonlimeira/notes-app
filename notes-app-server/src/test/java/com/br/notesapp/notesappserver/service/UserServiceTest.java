package com.br.notesapp.notesappserver.service;

import com.br.notesapp.notesappserver.MongoTestContainer;
import com.br.notesapp.notesappserver.dto.user.CreateUserRequestDTO;
import com.br.notesapp.notesappserver.dto.user.UpdateUserRequestDTO;
import com.br.notesapp.notesappserver.dto.user.UserModelDTO;
import com.br.notesapp.notesappserver.exception.LinkedNotesException;
import com.br.notesapp.notesappserver.exception.UserNotFoundException;
import com.br.notesapp.notesappserver.exception.UsernameAlreadyExistsException;
import com.br.notesapp.notesappserver.model.Note;
import com.br.notesapp.notesappserver.model.UserModel;
import com.br.notesapp.notesappserver.model.UserModelRole;
import com.br.notesapp.notesappserver.repository.NoteRepository;
import com.br.notesapp.notesappserver.utils.Mapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest extends MongoTestContainer {

    @Autowired
    UserService userService;

    @Autowired
    NoteRepository noteRepository;

    @Autowired
    Mapper mapper;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    void shouldReturnCreateUserWhenInsertUserIfUsernameIsNotInUse() {
        var createUserRequestDTO = new CreateUserRequestDTO("JohnDoe", "1234", Set.of(UserModelRole.EMPLOYEE));
        var newUser = userService.insert(createUserRequestDTO);
        assertEquals("JohnDoe", newUser.getUsername());
        assertEquals(List.of(UserModelRole.EMPLOYEE), newUser.getRoles());
    }

    @Test
    void shouldThrowUsernameAlreadyExpectionWhenInsertUserIfUsernameIsInUse() {
        var createUserRequestDTO = new CreateUserRequestDTO("John", "1234", Set.of(UserModelRole.EMPLOYEE));
        userService.insert(createUserRequestDTO);
        assertThrowsExactly(UsernameAlreadyExistsException.class, () -> userService.insert(createUserRequestDTO));
    }

    @Test
    void shouldReturnUserWhenFindByIDIfUserExists() {
        var createUserRequest = new CreateUserRequestDTO("JohnDoe", "1234", Set.of(UserModelRole.EMPLOYEE));
        var newUser = userService.insert(createUserRequest);

        var foundUser = userService.findById(newUser.getId());

        assertNotNull(foundUser);
        assertEquals(newUser.getId(), foundUser.getId());
        assertEquals(newUser.getUsername(), foundUser.getUsername());
    }

    @Test
    void shouldThrowUserNotFoundExpectionWhenFindByIDIfUserNotExists() {
        assertThrowsExactly(UserNotFoundException.class, () -> userService.findById("12345"));
    }

    @Test
    void shouldReturnEmptyListWhenFindAllIfThereIsNoUser() {
        var users = userService.findAll();

        assertTrue(users.isEmpty());
    }

    @Test
    void shouldReturnUserWhenFindAllIfThereIsAnyUser() {
        var createUserRequestDTO = new CreateUserRequestDTO("JohnDoe", "1234", Set.of(UserModelRole.EMPLOYEE));
        userService.insert(createUserRequestDTO);
        var users = userService.findAll();

        assertFalse(users.isEmpty());
    }

    @Test
    void shouldRemoveUserWhenDeleteByIdIfUserExists() {
        var createUserRequestDTO = new CreateUserRequestDTO("JohnDoe", "1234", Set.of(UserModelRole.EMPLOYEE));
        var user = userService.insert(createUserRequestDTO);

        userService.deleteById(user.getId());

        assertTrue(userService.findAll().isEmpty());
    }

    @Test
    void shouldThrowUserNotFoundExpectionWhenDeleteByIdIfUserNotExits() {
        assertThrowsExactly(UserNotFoundException.class, () -> userService.findById(UUID.randomUUID().toString()));
    }

    @Test
    void shouldThrowLinkedNotesExpectionWhenDeleteByIdIfUserNotExits() {
        var createUserRequestDTO = new CreateUserRequestDTO("JohnDoe", "1234", Set.of(UserModelRole.EMPLOYEE));
        var user = userService.insert(createUserRequestDTO);
        var note = new Note(mapper.map(user, UserModel.class), "Code.", "Learn through projects.");
        noteRepository.save(note);

        assertThrowsExactly(LinkedNotesException.class, () -> userService.deleteById(user.getId()));
    }

    @Test
    void shouldThrowUserNotFoundExpectionWhenUpdateIfUserNotExists() {
        UpdateUserRequestDTO updateUser = new UpdateUserRequestDTO("0", "John", "12345", true, Set.of(UserModelRole.EMPLOYEE, UserModelRole.ADMIN));
        assertThrowsExactly(UserNotFoundException.class, () -> userService.update(updateUser));
    }

    @Test
    void shouldThrowUsernameAlreadyExpectionWhenUpdateUserIfUsernameAlreadyInUse() {
        var createUser1 = new CreateUserRequestDTO("John", "1234", Set.of(UserModelRole.EMPLOYEE));
        var createUser2 = new CreateUserRequestDTO("JohnDoe", "123456", Set.of(UserModelRole.EMPLOYEE));
        var user1 = userService.insert(createUser1);
        userService.insert(createUser2);
        UpdateUserRequestDTO updateUser = new UpdateUserRequestDTO(user1.getId(), "JohnDoe", "12345", true, Set.of(UserModelRole.EMPLOYEE, UserModelRole.ADMIN));

        assertThrowsExactly(UsernameAlreadyExistsException.class, () -> userService.update(updateUser));
    }

    @Test
    void shouldUpdateUserWithSuccess() {
        var createUser1 = new CreateUserRequestDTO("John", "1234", Set.of(UserModelRole.EMPLOYEE));
        var user1 = userService.insert(createUser1);
        UpdateUserRequestDTO updateUser = new UpdateUserRequestDTO(user1.getId(), "JohnDoe", "12345", false, Set.of(UserModelRole.EMPLOYEE));

        userService.update(updateUser);
        UserModelDTO foundUser = userService.findById(user1.getId());

        assertEquals("JohnDoe", foundUser.getUsername());
        assertFalse(foundUser.isActive());
        assertEquals(List.of(UserModelRole.EMPLOYEE), foundUser.getRoles());
    }
}