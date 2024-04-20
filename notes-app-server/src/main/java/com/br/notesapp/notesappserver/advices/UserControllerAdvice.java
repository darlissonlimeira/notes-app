package com.br.notesapp.notesappserver.advices;

import com.br.notesapp.notesappserver.exception.UsernameAlreadyExistsException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
class UserControllerAdvice {

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    protected ResponseEntity<Object> handleusernameAlreadyExists(HttpServletRequest request, HttpServletResponse response, DuplicateKeyException exc) {
        return ResponseEntity.badRequest().build();
    }

}
