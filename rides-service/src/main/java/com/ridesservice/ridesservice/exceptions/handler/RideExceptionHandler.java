package com.ridesservice.ridesservice.exceptions.handler;

import com.ridesservice.ridesservice.dto.response.ExceptionResponse;
import com.ridesservice.ridesservice.dto.response.ValidationExceptionResponse;
import com.ridesservice.ridesservice.exceptions.AlreadyExistsException;
import com.ridesservice.ridesservice.exceptions.PromoCodeNotFoundException;
import com.ridesservice.ridesservice.exceptions.RideNotFoundException;
import com.ridesservice.ridesservice.exceptions.InvalidRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;

import static com.ridesservice.ridesservice.util.Messages.*;


@ControllerAdvice
public class RideExceptionHandler {
    @ExceptionHandler(value = {RideNotFoundException.class})
    public ResponseEntity<Object> handleNotFoundException(RideNotFoundException rideNotFoundException) {
        ExceptionResponse response =
                new ExceptionResponse(HttpStatus.NOT_FOUND,
                        rideNotFoundException.getMessage()
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
    public ResponseEntity<Object> handleRideAlreadyExists(AlreadyExistsException alreadyExistsException) {
        ExceptionResponse response =
                new ExceptionResponse(HttpStatus.CONFLICT,
                        alreadyExistsException.getMessage()
                );
        return new ResponseEntity<>(response, response.getStatus());
    }
    @ExceptionHandler(PromoCodeNotFoundException.class)
    public ResponseEntity<Object> handlePromoCodeNotFoundException(PromoCodeNotFoundException promoCodeNotFoundException) {
        ExceptionResponse response =
                new ExceptionResponse(HttpStatus.NOT_FOUND,
                        promoCodeNotFoundException.getMessage()
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
