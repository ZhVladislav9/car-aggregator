package com.passengerservice.passengerservice.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PassengerResponse {
    Integer id;
    String name;
    String surname;
    Double rating;
    String phone;
    String email;
}
