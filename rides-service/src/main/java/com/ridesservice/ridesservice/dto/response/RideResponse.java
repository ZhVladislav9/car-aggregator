package com.ridesservice.ridesservice.dto.response;

import com.ridesservice.ridesservice.models.RideStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RideResponse {
    Integer id;
    String pickUpAddress;
    String destinationAddress;
    Double price;
    Integer passengerId;
    Integer driverId;
    RideStatus status;
    LocalDateTime date;
}
