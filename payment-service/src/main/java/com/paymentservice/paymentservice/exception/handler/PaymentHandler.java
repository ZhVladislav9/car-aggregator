package com.paymentservice.paymentservice.exception.handler;


import com.paymentservice.paymentservice.dto.response.ExceptionResponse;
import com.paymentservice.paymentservice.dto.response.ValidationExceptionResponse;
import com.paymentservice.paymentservice.exception.AlreadyExistsException;
import com.paymentservice.paymentservice.exception.NotFoundException;
import com.paymentservice.paymentservice.exception.PaymentException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class PaymentHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationExceptionResponse handleMethodArgumentNotValid(MethodArgumentNotValidException methodArgumentNotValidException) {
        String VALIDATION_FAILED_MESSAGE = "Invalid request";
        Map<String, String> errors = methodArgumentNotValidException.getBindingResult().getAllErrors().stream()
                .map(error -> (FieldError) error)
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
        return new ValidationExceptionResponse(HttpStatus.BAD_REQUEST, VALIDATION_FAILED_MESSAGE, errors);
    }

    @ExceptionHandler(value = {PaymentException.class})
    @ResponseStatus(HttpStatus.PAYMENT_REQUIRED)
    public ExceptionResponse handlePaymentException(PaymentException paymentException) {
        return new ExceptionResponse(HttpStatus.PAYMENT_REQUIRED, paymentException.getMessage());
    }

    @ExceptionHandler(value = {AlreadyExistsException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handleAlreadyExistsException(AlreadyExistsException alreadyExistsException) {
        return new ExceptionResponse(HttpStatus.BAD_REQUEST, alreadyExistsException.getMessage());
    }

    @ExceptionHandler(value = {NotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponse handleNotFoundException(NotFoundException notFoundException) {
        return new ExceptionResponse(HttpStatus.NOT_FOUND, notFoundException.getMessage());
    }
}

