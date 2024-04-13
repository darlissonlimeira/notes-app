package com.br.notesapp.notesappserver.dto.auth;

import java.util.List;

public record TokenPayload(String username, List<String> authorities) {
}
