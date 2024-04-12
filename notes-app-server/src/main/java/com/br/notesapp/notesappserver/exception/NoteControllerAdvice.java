package com.br.notesapp.notesappserver.exception;

import com.br.notesapp.notesappserver.dto.api.ErrorData;
import com.br.notesapp.notesappserver.dto.api.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice
public class NoteControllerAdvice {

    @ExceptionHandler(DuplicateKeyException.class)
    protected ResponseEntity<Object> handleDuplicateKey(HttpServletRequest request, HttpServletResponse response, DuplicateKeyException exc) {
        ErrorData errorData = new ErrorData(HttpStatus.BAD_REQUEST.value(), exc.getMessage(), List.of(exc.getMessage()));
        return new ResponseEntity<>(new ErrorResponse(errorData), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {NoteNotFoundException.class})
    protected ResponseEntity<Object> handleNoteNotFound(HttpServletRequest request, HttpServletResponse response, RuntimeException exc) {
        ErrorData error = new ErrorData(HttpStatus.NOT_FOUND.value(), exc.getMessage(), List.of(exc.getMessage()));
        return new ResponseEntity<>(new ErrorResponse(error), HttpStatus.NOT_FOUND);
    }
}
