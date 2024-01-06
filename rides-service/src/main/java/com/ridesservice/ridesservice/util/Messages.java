package com.ridesservice.ridesservice.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Messages {
    public final String VALIDATION_FAILED_MESSAGE = "Invalid request";
    public final String RIDE_ALREADY_EXISTS_MESSAGE = "Ride already exists";
    public final String NOT_FOUND_WITH_ID_MESSAGE = "Ride with id %d was not found";
    public final String NOT_FOUND_WITH_PASSENGER_ID_MESSAGE = "Ride with passenger id %d was not found";
    public final String INVALID_SORTING_MESSAGE="Sorting request is not valid. Acceptable parameters are: %s";
}
