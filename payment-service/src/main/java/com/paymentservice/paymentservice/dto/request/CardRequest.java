package com.paymentservice.paymentservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardRequest {
    @NotBlank(message = "validation.cardRequest.cardNumber.empty")
    String cardNumber;
    @NotNull(message = "validation.cardRequest.expirationMonth.empty")
    int expMonth;
    @NotNull(message = "validation.cardRequest.expirationYear.empty")
    int expYear;
    @NotBlank(message = "validation.cardRequest.cvc.empty")
    @Length(max = 3,message = "validation.cardRequest.cvc.maxLength = 3")
    String cvc;
}
