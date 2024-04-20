package com.br.notesapp.notesappserver.service;

import com.br.notesapp.notesappserver.exception.UserNotFoundException;
import com.br.notesapp.notesappserver.model.UserModel;
import com.br.notesapp.notesappserver.repository.UserModelRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserAuthDetailsService {

    private final UserModelRepository repository;

    public UserAuthDetailsService(UserModelRepository repository) {
        this.repository = repository;
    }

    public UserDetails loadUserByUsername(String username) throws UserNotFoundException {
        Optional<UserModel> userModel = repository.findByUsername(username);
        var user = userModel.orElseThrow(() -> new UserNotFoundException(username));
        var authorities = user.getRoles().stream().map(r -> new SimpleGrantedAuthority(r.name())).toList();
        return User.builder().username(user.getUsername()).password(user.getPassword()).authorities(authorities).build();
    }
}
