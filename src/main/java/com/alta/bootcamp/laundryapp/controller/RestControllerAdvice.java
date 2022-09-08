package com.alta.bootcamp.laundryapp.controller;

import com.alta.bootcamp.laundryapp.dto.ResponseDTO;
import com.alta.bootcamp.laundryapp.exceptions.DataAlreadyExistException;
import com.alta.bootcamp.laundryapp.exceptions.ResourceNotFoundException;
import com.alta.bootcamp.laundryapp.exceptions.UnauthorizedException;
import com.alta.bootcamp.laundryapp.exceptions.ValidationErrorException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
// import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@EnableWebMvc
@ControllerAdvice
public class RestControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> globalExceptionHandler(Exception ex, WebRequest request) {
        ResponseDTO<Object> response = new ResponseDTO<>();
        response.setData(null);
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setMessage(ex.getMessage() + ", " + request.getDescription(false));

        return handleExceptionInternal(ex, response, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(ValidationErrorException.class)
    public ResponseEntity<?> validationErrorExceptionHandler(Exception ex, WebRequest request) {
        ResponseDTO<Object> response = new ResponseDTO<>();
        response.setData(null);
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setMessage(ex.getMessage() + ", " + request.getDescription(false));

        return handleExceptionInternal(ex, response, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> resourceNotFoundErrorExceptionHandler(Exception ex, WebRequest request) {
        ResponseDTO<Object> response = new ResponseDTO<>();
        response.setData(null);
        response.setStatus(HttpStatus.NOT_FOUND.value());
        response.setMessage(ex.getMessage() + ", " + request.getDescription(false));

        return handleExceptionInternal(ex, response, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(DataAlreadyExistException.class)
    public ResponseEntity<?> dataAlreadyExistsErrorExceptionHandler(Exception ex, WebRequest request) {
        ResponseDTO<Object> response = new ResponseDTO<>();
        response.setData(null);
        response.setStatus(HttpStatus.CONFLICT.value());
        response.setMessage(ex.getMessage() + ", " + request.getDescription(false));

        return handleExceptionInternal(ex, response, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<?> unauthorizedErrorExceptionHandler(Exception ex, WebRequest request) {
        ResponseDTO<Object> response = new ResponseDTO<>();
        response.setData(null);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setMessage(ex.getMessage() + ", " + request.getDescription(false));

        return handleExceptionInternal(ex, response, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }
}
