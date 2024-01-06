package com.passengerservice.passengerservice.exceptions;

import lombok.Getter;

import java.util.Map;

import static com.passengerservice.passengerservice.util.Messages.*;

@Getter
public class AlreadyExistsException extends RuntimeException{
    private final Map<String, String> errors;

    public AlreadyExistsException(Map<String, String> errors) {
        super(PASSENGER_ALREADY_EXISTS_MESSAGE);
        this.errors = errors;
    }
}
