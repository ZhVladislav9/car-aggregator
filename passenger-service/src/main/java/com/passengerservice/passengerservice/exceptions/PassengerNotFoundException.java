package com.passengerservice.passengerservice.exceptions;

import static com.passengerservice.passengerservice.util.Messages.*;

public class PassengerNotFoundException extends RuntimeException {
    public PassengerNotFoundException(Integer id) {
        super(String.format(NOT_FOUND_WITH_ID_MESSAGE, id));
    }
}
