package com.br.notesapp.notesappserver.exception;

public class LinkedNotesException extends RuntimeException {
    public LinkedNotesException() {
        super("User linked notes. Not able to remove the user.");
    }
}
