package com.br.notesapp.notesappserver.security;

import com.br.notesapp.notesappserver.model.UserModel;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class AccountDetails implements UserDetails {

    private String username;

    private String password;

    private List<GrantedAuthority> authorities;

    public AccountDetails(UserModel userModel) {
        this.username = userModel.getUsername();
        this.password = userModel.getPassword();
        this.authorities = userModel.getRoles().stream().map(a -> new SimpleGrantedAuthority(a.toString())).collect(Collectors.toList());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
