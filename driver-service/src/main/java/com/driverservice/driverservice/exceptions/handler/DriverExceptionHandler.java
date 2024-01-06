package com.driverservice.driverservice.exceptions.handler;

import com.driverservice.driverservice.exceptions.AlreadyExistsException;
import com.driverservice.driverservice.exceptions.DriverNotFoundException;
import com.driverservice.driverservice.dto.response.ExceptionResponse;
import com.driverservice.driverservice.dto.response.ValidationExceptionResponse;
import com.driverservice.driverservice.exceptions.InvalidRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;

import static com.driverservice.driverservice.util.Messages.*;


@ControllerAdvice
public class DriverExceptionHandler {
    @ExceptionHandler(value = {DriverNotFoundException.class})
    public ResponseEntity<Object> handleNotFoundException(DriverNotFoundException notFoundException) {
        ExceptionResponse response =
                new ExceptionResponse(HttpStatus.NOT_FOUND,
                        notFoundException.getMessage()
                );
        return new ResponseEntity<>(response, response.getStatus());
    }

    @ExceptionHandler(value = {InvalidRequestException.class})
    public ResponseEntity<Object> handleNotFoundException(InvalidRequestException invalidRequestException) {
        ExceptionResponse response =
                new ExceptionResponse(HttpStatus.BAD_REQUEST,
                        invalidRequestException.getMessage()
                );
        return new ResponseEntity<>(response, response.getStatus());
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<Object> handleDriverAlreadyExists(AlreadyExistsException alreadyExistsException) {
        ExceptionResponse response =
                new ExceptionResponse(HttpStatus.CONFLICT,
                        alreadyExistsException.getMessage()
                );
        return new ResponseEntity<>(response, response.getStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException methodArgumentNotValidException) {
        var errors = new HashMap<String, String>();
        methodArgumentNotValidException.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        ValidationExceptionResponse response = new ValidationExceptionResponse(HttpStatus.BAD_REQUEST, VALIDATION_FAILED_MESSAGE, errors);
        return new ResponseEntity<>(response, response.getStatus());
    }
}
