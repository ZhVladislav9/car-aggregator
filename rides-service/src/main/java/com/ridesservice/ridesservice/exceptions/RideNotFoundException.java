package com.ridesservice.ridesservice.exceptions;

import static com.ridesservice.ridesservice.util.Messages.*;

public class RideNotFoundException extends RuntimeException {

    public RideNotFoundException(String message, Integer id) {
        super(String.format(message, id));
    }
}
