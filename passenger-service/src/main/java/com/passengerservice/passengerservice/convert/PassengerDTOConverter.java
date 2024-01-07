package com.passengerservice.passengerservice.convert;

import com.passengerservice.passengerservice.dto.request.PassengerRequest;
import com.passengerservice.passengerservice.dto.response.PassengerResponse;
import com.passengerservice.passengerservice.models.Passenger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class PassengerDTOConverter {
    @Autowired
    private ModelMapper modelMapper;

    public PassengerResponse convertPassengerToPassengerResponse(Passenger passenger){
        return modelMapper.map(passenger,PassengerResponse.class);
    }
    public Passenger convertPassengerResponseToPassenger(PassengerResponse passengerResponse){
        return modelMapper.map(passengerResponse,Passenger.class);
    }
    public PassengerRequest convertPassengerToPassengerRequest(Passenger passenger){
        return modelMapper.map(passenger,PassengerRequest.class);
    }
    public Passenger convertPassengerRequestToPassenger(PassengerRequest passengerRequest){
        return modelMapper.map(passengerRequest,Passenger.class);
    }
}