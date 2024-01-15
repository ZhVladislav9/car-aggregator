package com.paymentservice.paymentservice.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Messages {
    public final String ALREADY_EXISTS_MESSAGE = "Customer with id %s already exists";
    public final String NOT_FOUND_MESSAGE = "Customer with id %s not found";
    public final String NOT_ENOUGH_MONEY_MESSAGE = "Not enough money in the account";

}