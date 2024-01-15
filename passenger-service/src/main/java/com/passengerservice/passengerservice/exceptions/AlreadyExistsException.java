package com.passengerservice.passengerservice.exceptions;

import lombok.Getter;

@Getter
public class AlreadyExistsException extends RuntimeException{

    public AlreadyExistsException(String message) {
        super(message);
    }
}
