package com.ridesservice.ridesservice.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PromoCodeRequest {
    @NotBlank(message = "{PromoCode name is empty}")
    String name;
    @NotNull(message = "{PromoCode coefficient is empty}")
    @DecimalMin(value = "0.1", message = "{coefficient min value} 0.1")
    @DecimalMax(value = "1", message = "{coefficient max value} 1")
    Double coefficient;
}
