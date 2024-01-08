package com.driverservice.driverservice.exceptions;

import lombok.Getter;

import java.util.Map;

import static com.driverservice.driverservice.util.Messages.*;

@Getter
public class AlreadyExistsException extends RuntimeException{
    public AlreadyExistsException(String message) {
        super(message);
    }
}
