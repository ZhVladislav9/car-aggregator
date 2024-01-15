package com.paymentservice.paymentservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChargeRequest {
    @NotNull(message = "validation.chargeRequest.amount.empty")
    Double amount;
    @NotBlank(message = "{validation.chargeRequest.currency.empty}")
    String currency;
    @NotBlank(message = "validation.chargeRequest.cardToken.empty")
    String cardToken;
}