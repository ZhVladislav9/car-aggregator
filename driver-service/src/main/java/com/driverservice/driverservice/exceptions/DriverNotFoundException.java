package com.driverservice.driverservice.exceptions;

import static com.driverservice.driverservice.util.Messages.*;

public class DriverNotFoundException extends RuntimeException {

    public DriverNotFoundException(Integer id) {
        super(String.format(NOT_FOUND_WITH_ID_MESSAGE, id));
    }
}
