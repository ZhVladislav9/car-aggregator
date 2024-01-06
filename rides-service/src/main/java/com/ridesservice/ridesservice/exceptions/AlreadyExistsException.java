package com.ridesservice.ridesservice.exceptions;

import lombok.Getter;

import java.util.Map;

import static com.ridesservice.ridesservice.util.Messages.*;

@Getter
public class AlreadyExistsException extends RuntimeException{
    private final Map<String, String> errors;

    public AlreadyExistsException(Map<String, String> errors) {
        super(RIDE_ALREADY_EXISTS_MESSAGE);
        this.errors = errors;
    }
}
