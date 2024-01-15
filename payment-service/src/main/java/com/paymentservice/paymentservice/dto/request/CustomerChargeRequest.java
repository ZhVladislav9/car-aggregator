package com.paymentservice.paymentservice.dto.request;

import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerChargeRequest {
    @NotNull(message = "validation.customerChargeRequest.amount.empty")
    @DecimalMin(value = "0.01", message = "validation.customerChargeRequest.amount.min = 0.01")
    @DecimalMax(value = "5000", message = "validation.customerChargeRequest.amount.max = 5000")
    Double amount;
    @NotBlank(message = "{validation.customerChargeRequest.currency.empty}")
    String currency;
    @NotNull(message = "validation.customerChargeRequest.passengerId.empty")
    @Min(value = 1, message = "validation.customerChargeRequest.passengerId.min = 1")
    long passengerId;
}
