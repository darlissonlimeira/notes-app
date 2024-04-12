package com.br.notesapp.notesappserver.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public class CreateNoteRequestDTO {

    @NotBlank
    private String title;

    @NotBlank
    private String text;

    @NotBlank
    @JsonProperty("user_id")
    private String userId;

    public CreateNoteRequestDTO() {
    }

    public CreateNoteRequestDTO(String title, String text, String userId) {
        this.title = title;
        this.text = text;
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public String getUserId() {
        return userId;
    }
}
