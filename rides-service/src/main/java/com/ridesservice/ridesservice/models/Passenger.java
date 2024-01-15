package com.ridesservice.ridesservice.models;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Passenger {
    Integer id;
    String name;
    String surname;
    Double rating;
    String phone;
    String email;
}
