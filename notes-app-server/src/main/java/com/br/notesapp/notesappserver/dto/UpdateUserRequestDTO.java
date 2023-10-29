package com.br.notesapp.notesappserver.dto;

import com.br.notesapp.notesappserver.model.UserModelRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.Set;

public class UpdateUserRequestDTO {

    @NotBlank
    String id;

    @NotBlank(message = "username cannot be blank")
    @Size(min = 3, max = 20, message = "username length must be between 3 and 20")
    String username;

    String password;

    boolean active;

    @NotEmpty()
    Set<UserModelRole> roles;

    public UpdateUserRequestDTO() {
    }

    public UpdateUserRequestDTO(String id, String username, String password, boolean active, Set<UserModelRole> roles) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.active = active;
        this.roles = roles;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
    
    public boolean isActive() {
        return active;
    }

    public Set<UserModelRole> getRoles() {
        return roles;
    }

}
