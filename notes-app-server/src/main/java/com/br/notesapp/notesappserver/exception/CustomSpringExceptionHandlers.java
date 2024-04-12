package com.br.notesapp.notesappserver.exception;

import com.br.notesapp.notesappserver.dto.api.ErrorData;
import com.br.notesapp.notesappserver.dto.api.ErrorResponse;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class CustomSpringExceptionHandlers extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exc, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<String> errors = exc.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        ErrorData error = new ErrorData(status.value(), "invalid request", errors);

        return new ResponseEntity<>(new ErrorResponse(error), status);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ErrorData error = new ErrorData(status.value(), "Invalid request body", List.of(ex.getMessage()));
        return new ResponseEntity<>(new ErrorResponse(error), status);
    }
}


