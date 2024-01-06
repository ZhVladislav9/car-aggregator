package com.ridesservice.ridesservice.dto.request;

import jakarta.validation.constraints.Min;
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
    @NotBlank(message = "Pick-up address is empty")
    String pickUpAddress;
    @NotBlank(message = "Destination address is empty")
    String destinationAddress;
//    @Min(value = 1,message = "Min value is 1")
//    @NotNull(message = "Price is empty")
//    Double price;
    @Range(min = 1, message = "Min value is 1")
    @NotNull(message = "Passenger id is empty")
    Integer passengerId;
//    @Range(min = 1, message = "Min value is 1")
//    @NotNull(message = "Driver id is empty")
//    Integer driverId;
}
