package com.br.notesapp.notesappserver.service;

import com.br.notesapp.notesappserver.dto.user.CreateUserRequestDTO;
import com.br.notesapp.notesappserver.dto.user.UpdateUserRequestDTO;
import com.br.notesapp.notesappserver.dto.user.UserModelDTO;
import com.br.notesapp.notesappserver.exception.LinkedNotesException;
import com.br.notesapp.notesappserver.exception.UserNotFoundException;
import com.br.notesapp.notesappserver.exception.UsernameAlreadyExistsException;
import com.br.notesapp.notesappserver.model.UserModel;
import com.br.notesapp.notesappserver.repository.NoteRepository;
import com.br.notesapp.notesappserver.repository.UserModelRepository;
import com.br.notesapp.notesappserver.utils.Mapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserModelRepository repository;

    private final NoteRepository noteRepository;

    private final Mapper mapper;

    private final PasswordEncoder passwordEncoder;

    public UserService(UserModelRepository repository, NoteRepository noteRepository, Mapper mapper, PasswordEncoder passwordEncorder) {
        this.repository = repository;
        this.noteRepository = noteRepository;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncorder;
    }

    public UserModelDTO insert(CreateUserRequestDTO request) {
        var usernameIsPresent = repository.findByUsername(request.getUsername()).isPresent();

        if (usernameIsPresent) throw new UsernameAlreadyExistsException();

        UserModel user = mapper.map(request, UserModel.class);
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        UserModel userModel = repository.insert(user);
        return mapper.map(userModel, UserModelDTO.class);
    }

    public UserModelDTO findById(String userId) {
        UserModel user = repository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(String.format("user not found with id: %s", userId)));

        return mapper.map(user, UserModelDTO.class);
    }

    public List<UserModelDTO> findAll() {
        List<UserModel> users = repository.findAll();
        return users.stream().map(user -> mapper.map(user, UserModelDTO.class)).toList();
    }

    public void deleteById(String userId) {
        int notesCount = noteRepository.findByUserId(userId).size();
        if (notesCount > 0) throw new LinkedNotesException();

        UserModel user = repository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(String.format("user not found with id: %s", userId)));

        repository.delete(user);
    }

    public void update(UpdateUserRequestDTO request) {
        repository.findById(request.getId())
                .orElseThrow(() -> new UserNotFoundException(String.format("user not found with id: %s", request.getId())));

        boolean usernameExists = repository.findByUsername(request.getUsername()).isPresent();
        if (usernameExists) throw new UsernameAlreadyExistsException();

        UserModel updatedUser = mapper.map(request, UserModel.class);
        repository.save(updatedUser);
    }

}
