package com.br.notesapp.notesappserver.advices;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.br.notesapp.notesappserver.exception.InvalidBearerTokenException;
import com.br.notesapp.notesappserver.exception.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AuthControllerAdvice {

    @ExceptionHandler(value = {UserNotFoundException.class, BadCredentialsException.class})
    protected ResponseEntity<?> handleUserNotFound(HttpServletRequest request, HttpServletResponse response, RuntimeException exc) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ExceptionHandler(value = {TokenExpiredException.class})
    protected ResponseEntity<?> handleTokenExpired(HttpServletRequest request, HttpServletResponse response, RuntimeException exc) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @ExceptionHandler(value = {JWTVerificationException.class, InvalidBearerTokenException.class})
    protected ResponseEntity<?> handleJWTVerification(HttpServletRequest request, HttpServletResponse response, RuntimeException exc) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

}
