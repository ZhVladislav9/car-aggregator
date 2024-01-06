package com.ridesservice.ridesservice.exceptions;

import static com.ridesservice.ridesservice.util.Messages.*;

public class RideNotFoundException extends RuntimeException {

    public RideNotFoundException(Integer id) {
        super(String.format(NOT_FOUND_WITH_ID_MESSAGE, id));
    }
}
