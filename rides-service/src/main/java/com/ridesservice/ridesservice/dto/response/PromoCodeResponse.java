package com.ridesservice.ridesservice.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PromoCodeResponse {
    Integer id;
    String name;
    Double coefficient;
}
