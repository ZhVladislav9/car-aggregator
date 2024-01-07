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
    @NotBlank(message = "{name is empty}")
    @Pattern(regexp = "^[^\\d]+$", message = "{name has digits}")
    String name;
    @NotBlank(message = "{surname is empty}")
    @Pattern(regexp = "^[^\\d]+$", message = "{surname has digits}")
    String surname;
    @NotNull(message = "{rating is 0}")
    @Min(value = 1,message = "Min value is 1")
    @Max(value = 5,message = "Max value is 5")
    Double rating;
    @NotBlank(message = "{email is empty}")
    @Email(message = "{email is not valid}")
    String email;
    @NotBlank(message = "{phone is empty}")
    @Pattern(regexp = "^\\d{10,15}$", message = "{phone is not valid}")
    String phone;
    @NotNull(message = "{availability is empty}")
    Boolean isAvailable;
}
