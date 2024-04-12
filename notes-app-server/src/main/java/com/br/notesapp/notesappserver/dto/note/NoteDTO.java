package com.br.notesapp.notesappserver.dto.note;

import jakarta.validation.constraints.NotBlank;

import java.util.Date;
import java.util.Objects;

public class NoteDTO {

    @NotBlank
    private String user;

    @NotBlank
    private String id;

    @NotBlank
    private String title;

    @NotBlank
    private String text;

    private boolean completed = false;

    @NotBlank
    private Date createdAt;

    @NotBlank
    private Date updatedAt;

    public NoteDTO() {
    }

    public NoteDTO(String user, String id, String title, String text, boolean completed, Date createdAt, Date updatedAt) {
        this.user = user;
        this.id = id;
        this.title = title;
        this.text = text;
        this.completed = completed;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public NoteDTO(String title, String text) {
        this.title = title;
        this.text = text;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NoteDTO noteDTO = (NoteDTO) o;
        return Objects.equals(id, noteDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
