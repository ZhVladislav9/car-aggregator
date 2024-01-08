package com.ridesservice.ridesservice.exceptions;

public class RideNotFoundException extends RuntimeException {

    public RideNotFoundException(String message, Integer id) {
        super(String.format(message, id));
    }
}
