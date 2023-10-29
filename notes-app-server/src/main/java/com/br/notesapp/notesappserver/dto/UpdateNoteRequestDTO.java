package com.br.notesapp.notesappserver.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.Objects;

public class UpdateNoteRequestDTO implements Serializable {

    @NotBlank
    private String id;
    @NotBlank
    private String title;

    @NotBlank
    private String text;

    @NotNull
    private boolean completed;

    public UpdateNoteRequestDTO() {
    }

    public UpdateNoteRequestDTO(String id, String title, String text, boolean completed) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.completed = completed;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public boolean isCompleted() {
        return completed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UpdateNoteRequestDTO that = (UpdateNoteRequestDTO) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
