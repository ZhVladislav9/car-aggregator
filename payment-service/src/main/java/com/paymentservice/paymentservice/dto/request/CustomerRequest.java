package com.paymentservice.paymentservice.dto.request;

import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerRequest {
    @NotBlank(message = "validation.CustomerRequest.name.empty")
    @Pattern(regexp = "^[^\\d]+$", message = "validation.CustomerRequest.name.hasDigits")
    String name;
    @Email(message = "validation.CustomerRequest.email.notValid")
    @NotBlank(message = "validation.CustomerRequest.email.empty")
    String email;
    @Pattern(regexp = "^\\d{10,15}$", message = "validation.CustomerRequest.phone.notValid")
    @NotBlank(message = "validation.CustomerRequest.phone.empty")
    String phone;
    @NotNull(message = "validation.CustomerRequest.passengerId.empty")
    @Min(value = 1, message = "validation.CustomerRequest.passengerId.min = 1")
    long passengerId;
    @NotNull(message = "validation.CustomerRequest.balance.empty")
    @DecimalMin(value = "0.01", message = "validation.CustomerRequest.balance.min = 0.01")
    @DecimalMax(value = "5000", message = "validation.CustomerRequest.balance.max = 5000")
    Double balance;
}
