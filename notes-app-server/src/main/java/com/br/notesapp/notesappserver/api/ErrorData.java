package com.br.notesapp.notesappserver.api;

import java.util.List;

public record ErrorData(int code, String message, List<String> errors) {
}
