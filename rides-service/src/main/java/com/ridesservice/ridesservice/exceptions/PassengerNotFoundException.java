package com.ridesservice.ridesservice.exceptions;


import static com.ridesservice.ridesservice.util.Messages.*;

public class PassengerNotFoundException extends RuntimeException {

    public PassengerNotFoundException(Integer id) {
        super(String.format(PASSENGER_NOT_FOUND_WITH_ID_MESSAGE, id));
    }
}
