package com.driverservice.driverservice.dto.request;

import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DriverRequest {
    @NotBlank(message = "validation.driver.name.empty")
    @Pattern(regexp = "^[^\\d]+$", message = "validation.driver.name.hasDigits")
    String name;
    @NotBlank(message = "validation.driver.surname.empty")
    @Pattern(regexp = "^[^\\d]+$", message = "validation.driver.surname.hasDigits")
    String surname;
    @NotNull(message = "validation.driver.rating.isNull")
    @Min(value = 1,message = "validation.driver.rating.min = 1")
    @Max(value = 5,message = "validation.driver.rating.max = 5")
    Double rating;
    @NotBlank(message = "validation.driver.phone.empty")
    @Pattern(regexp = "^\\d{10,15}$", message = "validation.driver.phone.notValid")
    String phone;
    @NotBlank(message = "validation.driver.email.empty")
    @Email(message = "validation.driver.email.notValid")
    String email;
    @NotNull(message = "validation.driver.isAvailable.empty")
    Boolean isAvailable;
}
