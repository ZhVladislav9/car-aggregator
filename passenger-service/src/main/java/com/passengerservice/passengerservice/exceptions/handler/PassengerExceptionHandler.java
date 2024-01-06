package com.passengerservice.passengerservice.exceptions.handler;

import com.passengerservice.passengerservice.dto.response.ExceptionResponse;
import com.passengerservice.passengerservice.dto.response.ValidationExceptionResponse;
import com.passengerservice.passengerservice.exceptions.AlreadyExistsException;
import com.passengerservice.passengerservice.exceptions.InvalidRequestException;
import com.passengerservice.passengerservice.exceptions.PassengerNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static com.passengerservice.passengerservice.util.Messages.*;

import java.util.HashMap;

@ControllerAdvice
public class PassengerExceptionHandler {
    @ExceptionHandler(value = {PassengerNotFoundException.class})
    public ResponseEntity<Object> handleNotFoundException(PassengerNotFoundException notFoundException) {
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
    public ResponseEntity<Object> handlePassengerAlreadyExists(AlreadyExistsException alreadyExistsException) {
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
