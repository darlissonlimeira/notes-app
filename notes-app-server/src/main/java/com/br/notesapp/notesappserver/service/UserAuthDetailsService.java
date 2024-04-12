package com.br.notesapp.notesappserver.service;

import com.br.notesapp.notesappserver.dto.auth.UserAuthDetails;
import com.br.notesapp.notesappserver.model.UserModel;
import com.br.notesapp.notesappserver.repository.UserModelRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserAuthDetailsService {

    private final UserModelRepository repository;

    public UserAuthDetailsService(UserModelRepository repository) {
        this.repository = repository;
    }

    public Optional<UserAuthDetails> loadUserByUsername(String username) {
        Optional<UserModel> user = repository.findByUsername(username);
        return user.map(UserAuthDetails::new);
    }
}
