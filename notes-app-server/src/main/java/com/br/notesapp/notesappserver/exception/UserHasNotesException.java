package com.br.notesapp.notesappserver.exception;

public class UserHasNotesException extends RuntimeException {
    public UserHasNotesException(String message) {
        super(message);
    }
}
