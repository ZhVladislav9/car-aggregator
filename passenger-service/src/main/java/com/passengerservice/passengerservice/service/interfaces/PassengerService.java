package com.passengerservice.passengerservice.service.interfaces;

import com.passengerservice.passengerservice.dto.request.PassengerRequest;
import com.passengerservice.passengerservice.dto.response.PassengerResponse;
import com.passengerservice.passengerservice.dto.response.PassengersListResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PassengerService {

    public PassengersListResponse getPassengers(List<PassengerResponse> passengers);
    public PassengerResponse getPassengerById(int id);
    public PassengerResponse updatePassenger(int id, PassengerRequest passengerRequest);
    public ResponseEntity<HttpStatus> deletePassenger(int id);
    public PassengerResponse addPassenger(PassengerRequest passengerRequest);
}
