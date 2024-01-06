package com.passengerservice.passengerservice.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PassengersListResponse {
    Integer total;
    String sortedByField;
    List<PassengerResponse> passengers;
}
