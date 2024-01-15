package com.passengerservice.passengerservice.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Builder
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PassengersListResponse {
    Integer page;
    Integer size;
    Integer total;
    String sortedByField;
    List<PassengerResponse> passengers;
}
