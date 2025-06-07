package com.substring.foodies.exception;

import com.substring.foodies.dto.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;

import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private Logger logger= LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFound.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFound ex)
    {
        ErrorResponse error=ErrorResponse.builder()
                            .message(ex.getMessage())
                            .status(HttpStatus.NO_CONTENT)
                            .build();

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(BadCredentialsException ex)
    {
        ErrorResponse error=ErrorResponse.builder()
                .message("Invalid Username or Password")
                .status(HttpStatus.BAD_REQUEST)
                .build();

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NullPointerException.class)
    public String handleNullPointerException(NullPointerException ex)
    {
        logger.error(ex.getMessage());
        ex.printStackTrace();
        return "Your number is Null";
    }

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<String> handleFileNotFoundException(NullPointerException ex)
    {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }

    @ExceptionHandler(InvalidPathException.class)
    public ResponseEntity<String> handleInvalidPathException(InvalidPathException ex)
    {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }

//    Exception handler in a Spring Boot application that catches validation errors
//    when an incoming request fails @Valid validation.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex)
    {
//        A HashMap is created to store field names as keys and validation error messages as values.
        Map<String, String> errorMap = new HashMap<>();

//        .getBindingResult fetches all the validation errors from the error message that we are getting
//        Retrieves all validation errors as a list of ObjectError.
        List<ObjectError> allErrors=ex.getBindingResult().getAllErrors();


//        Iterates through each error.
//        Casts ObjectError to FieldError to access the field name.
//        Gets the field name (getField()) and error message (getDefaultMessage()).
//        Stores them in errorMap.

        allErrors.forEach(error -> {
            String fieldName=((FieldError) error).getField();
            String message=error.getDefaultMessage();
            errorMap.put(fieldName, message);
        });

        return errorMap;
    }



}
