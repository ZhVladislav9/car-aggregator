package com.ridesservice.ridesservice.exceptions;

import lombok.Getter;

import java.util.Map;

import static com.ridesservice.ridesservice.util.Messages.*;

@Getter
public class AlreadyExistsException extends RuntimeException{
    public AlreadyExistsException(String message) {
        super(message);
    }
}
