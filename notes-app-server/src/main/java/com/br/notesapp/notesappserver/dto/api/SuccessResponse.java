package com.br.notesapp.notesappserver.dto.api;

import java.time.LocalDateTime;

public record SuccessResponse(LocalDateTime timestamp, Object data) {
    public SuccessResponse(Object data) {
        this(LocalDateTime.now(), data);
    }
}
