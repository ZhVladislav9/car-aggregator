package com.ridesservice.ridesservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Range;


@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RideRequest {
    @NotBlank(message = "validation.ride.pickUpAddress.empty")
    String pickUpAddress;
    @NotBlank(message = "validation.ride.destinationAddress.empty")
    String destinationAddress;
    @Range(min = 1, message = "validation.ride.passengerId.notValid")
    @NotNull(message = "validation.ride.passengerId.empty")
    Integer passengerId;
}
