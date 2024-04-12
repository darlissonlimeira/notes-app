package com.br.notesapp.notesappserver.dto.auth;

import com.br.notesapp.notesappserver.model.UserModel;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserAuthDetails {

    private String userId;
    private String username;
    private String password;
    private List<GrantedAuthority> authorities = new ArrayList<>();

    public UserAuthDetails(UserModel user) {
        userId = user.getId();
        username = user.getUsername();
        password = user.getPassword();
        user.getRoles().forEach(userModelRole -> {
            authorities.add(new SimpleGrantedAuthority(userModelRole.toString()));
        });
    }

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String toString() {
        return "UserAuthDetails{" +
                "userId='" + userId + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", authorities=" + authorities +
                '}';
    }
}
