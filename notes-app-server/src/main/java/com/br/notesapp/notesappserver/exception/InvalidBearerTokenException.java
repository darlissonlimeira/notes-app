package com.br.notesapp.notesappserver.exception;

public class InvalidBearerTokenException extends RuntimeException {

    public InvalidBearerTokenException() {
        super("Invalid bearer token.");
    }
}
