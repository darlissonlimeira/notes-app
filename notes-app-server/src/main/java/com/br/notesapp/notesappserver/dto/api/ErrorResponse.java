package com.br.notesapp.notesappserver.dto.api;

import java.time.LocalDateTime;

public record ErrorResponse(LocalDateTime timestamp, ErrorData error) {
    public ErrorResponse(ErrorData error) {
        this(LocalDateTime.now(), error);
    }
}
