package com.br.notesapp.notesappserver.dto.user;

import com.br.notesapp.notesappserver.model.UserModelRole;
import jakarta.validation.constraints.Size;

import java.util.Set;


public class CreateUserRequestDTO {

    @Size(min = 5, max = 30, message = "username length must be between 5 and 30")
    private String username;

    @Size(min = 8, message = "password lenght must be at least 8")
    private String password;

    @Size(min = 1)
    private Set<UserModelRole> roles;


    public CreateUserRequestDTO() {
    }

    public CreateUserRequestDTO(String username, String password, Set<UserModelRole> roles) {
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Set<UserModelRole> getRoles() {
        return roles;
    }
}
