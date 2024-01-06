package com.driverservice.driverservice.exceptions;

import lombok.Getter;

import java.util.Map;

import static com.driverservice.driverservice.util.Messages.*;

@Getter
public class AlreadyExistsException extends RuntimeException{
    private final Map<String, String> errors;

    public AlreadyExistsException(Map<String, String> errors) {
        super(DRIVER_ALREADY_EXISTS_MESSAGE);
        this.errors = errors;
    }
}
