package com.br.notesapp.notesappserver.security;

import com.br.notesapp.notesappserver.model.UserModel;
import com.br.notesapp.notesappserver.repository.UserModelRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountDetailsService implements UserDetailsService {

    private final UserModelRepository repository;

    public AccountDetailsService(UserModelRepository repository) {
        this.repository = repository;
    }

    @Override
    public AccountDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserModel> userAccount = repository.findByUsername(username);
        if (userAccount.isEmpty()) throw new UsernameNotFoundException(username);
        return new AccountDetails(userAccount.get());
    }
}
