package com.ridesservice.ridesservice.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Messages {
    public final String VALIDATION_FAILED_MESSAGE = "Invalid request";
    public final String NOT_FOUND_WITH_ID_MESSAGE = "Ride with id %d was not found";
    public final String NOT_FOUND_WITH_PASSENGER_ID_MESSAGE = "Ride with passenger id %d was not found";
    public final String NOT_FOUND_WITH_DRIVER_ID_MESSAGE = "Ride with driver id %d was not found";
    public final String INVALID_SORTING_MESSAGE="Sorting request is not valid. Acceptable parameters are: %s";
    public final String PROMO_CODE_WITH_NAME_EXISTS_MESSAGE = "PromoCode with name %s already exists";
    public final String PROMO_CODE_NOT_FOUND_WITH_ID_MESSAGE = "PromoCode with id %s was not found";
    public final String PROMO_CODE_NOT_FOUND_WITH_NAME_MESSAGE = "PromoCode with name %s was not found";
}
