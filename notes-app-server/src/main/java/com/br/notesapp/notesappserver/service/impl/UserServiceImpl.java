package com.br.notesapp.notesappserver.service.impl;

import com.br.notesapp.notesappserver.dto.user.CreateUserRequestDTO;
import com.br.notesapp.notesappserver.dto.user.UpdateUserRequestDTO;
import com.br.notesapp.notesappserver.dto.user.UserModelDTO;
import com.br.notesapp.notesappserver.exception.UserHasNotesException;
import com.br.notesapp.notesappserver.exception.UserNotFoundException;
import com.br.notesapp.notesappserver.model.Note;
import com.br.notesapp.notesappserver.model.UserModel;
import com.br.notesapp.notesappserver.repository.NoteRepository;
import com.br.notesapp.notesappserver.repository.UserModelRepository;
import com.br.notesapp.notesappserver.service.UserService;
import com.br.notesapp.notesappserver.utils.Mapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserModelRepository repository;

    private final NoteRepository noteRepository;

    private final Mapper mapper;

    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserModelRepository repository, NoteRepository noteRepository, Mapper mapper, PasswordEncoder passwordEncorder) {
        this.repository = repository;
        this.noteRepository = noteRepository;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncorder;
    }

    @Override
    public UserModelDTO insert(CreateUserRequestDTO request) {
        UserModel user = mapper.map(request, UserModel.class);
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        UserModel userModel = repository.insert(user);
        return mapper.map(userModel, UserModelDTO.class);
    }

    @Override
    public UserModelDTO findById(String userId) {
        Optional<UserModel> user = repository.findById(userId);
        if (user.isEmpty()) {
            throw new UserNotFoundException(String.format("user not found with id: %s", userId));
        }
        return mapper.map(user.get(), UserModelDTO.class);
    }

    @Override
    public List<UserModelDTO> find() {
        List<UserModel> users = repository.findAll();
        return users.stream().map(user -> mapper.map(user, UserModelDTO.class)).collect(Collectors.toList());
    }

    @Override
    public void deleteOne(String userId) {
        Optional<UserModel> user = repository.findById(userId);
        if (user.isEmpty()) {
            throw new UserNotFoundException(String.format("user not found with id: %s", userId));
        }

        List<Note> notes = noteRepository.findAllByUserId(userId);
        if (notes.size() > 0) {
            throw new UserHasNotesException("user has assigned notes");
        }
        repository.delete(user.get());
    }

    @Override
    public void update(UpdateUserRequestDTO request) {
        Optional<UserModel> user = repository.findById(request.getId());
        if (user.isEmpty()) {
            throw new UserNotFoundException(String.format("user not found with id: %s", request.getId()));
        }
        UserModel userModel = mapper.map(request, UserModel.class);
        boolean passwordEmpty = request.getPassword().length() < 4;
        if (passwordEmpty) {
            userModel.setPassword(user.get().getPassword());
        } else {
            userModel.setPassword(passwordEncoder.encode(userModel.getPassword()));
        }
        repository.save(userModel);
    }

}
