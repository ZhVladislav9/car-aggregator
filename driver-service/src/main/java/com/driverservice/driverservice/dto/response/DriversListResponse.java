package com.driverservice.driverservice.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DriversListResponse {
    Integer page;
    Integer size;
    Integer total;
    String sortedByField;
    List<DriverResponse> drivers;
}