package com.passengerservice.passengerservice.exceptions;

import lombok.Getter;

import java.util.Map;

import static com.passengerservice.passengerservice.util.Messages.*;
@Getter
public class AlreadyExistsException extends RuntimeException{

    public AlreadyExistsException(String message) {
        super(message);
    }
}
