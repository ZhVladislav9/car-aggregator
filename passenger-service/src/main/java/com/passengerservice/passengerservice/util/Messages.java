package com.passengerservice.passengerservice.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Messages {
    public final String VALIDATION_FAILED_MESSAGE = "Invalid request";
    public final String NOT_FOUND_WITH_ID_MESSAGE = "Passenger with id %d was not found";
    public final String PASSENGER_WITH_EMAIL_EXISTS_MESSAGE = "Passenger with email %s already exists";
    public final String PASSENGER_WITH_PHONE_EXISTS_MESSAGE = "Passenger with phone %s already exists";
    public final String INVALID_SORTING_MESSAGE="Sorting request is not valid. Acceptable parameters are: %s";

}
