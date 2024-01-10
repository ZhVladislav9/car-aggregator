package com.passengerservice.passengerservice.dto.request;

import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PassengerRequest {
    @NotBlank(message = "validation.passenger.name.empty")
    @Pattern(regexp = "^[^\\d]+$", message = "validation.passenger.name.hasDigits")
    String name;
    @NotBlank(message = "validation.passenger.surname.empty")
    @Pattern(regexp = "^[^\\d]+$", message = "validation.passenger.surname.hasDigits")
    String surname;
    @NotNull(message = "validation.passenger.rating.isNull")
    @Min(value = 1,message = "validation.passenger.rating.min = 1")
    @Max(value = 5,message = "validation.passenger.rating.max = 5")
    Double rating;
    @NotBlank(message = "validation.passenger.phone.empty")
    @Pattern(regexp = "^\\d{10,15}$", message = "validation.passenger.phone.notValid")
    String phone;
    @NotBlank(message = "validation.passenger.email.empty")
    @Email(message = "validation.passenger.email.notValid")
    String email;
}
