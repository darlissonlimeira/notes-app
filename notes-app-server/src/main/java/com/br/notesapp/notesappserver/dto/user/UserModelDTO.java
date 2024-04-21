package com.br.notesapp.notesappserver.dto.user;

import com.br.notesapp.notesappserver.model.UserModelRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.List;

public class UserModelDTO implements Serializable {

    @NotBlank
    private String id;

    @NotBlank
    private String username;

    @NotEmpty
    @NotNull
    private List<UserModelRole> roles;

    @NotNull
    private boolean active;

    public UserModelDTO() {
    }

    public UserModelDTO(String id, String username, List<UserModelRole> roles, boolean active) {
        this.id = id;
        this.username = username;
        this.roles = roles;
        this.active = active;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<UserModelRole> getRoles() {
        return roles;
    }

    public void setRoles(List<UserModelRole> roles) {
        this.roles = roles;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "UserModelDTO{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", roles=" + roles +
                ", active=" + active +
                '}';
    }
}
