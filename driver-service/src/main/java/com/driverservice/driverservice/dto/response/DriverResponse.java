package com.driverservice.driverservice.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DriverResponse {
    Integer id;
    String name;
    String surname;
    Double rating;
    String phone;
    Boolean isAvailable;
}