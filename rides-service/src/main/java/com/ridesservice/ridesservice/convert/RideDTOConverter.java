package com.ridesservice.ridesservice.convert;

import com.ridesservice.ridesservice.dto.request.RideRequest;
import com.ridesservice.ridesservice.dto.response.RideResponse;
import com.ridesservice.ridesservice.models.Ride;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class RideDTOConverter {
    @Autowired
    private ModelMapper modelMapper;
    public RideResponse convertRideToRideResponse(Ride ride){
        return modelMapper.map(ride, RideResponse.class);
    }
    public Ride convertRideResponseToRide(RideResponse rideResponse){
        return modelMapper.map(rideResponse,Ride.class);
    }
    public RideRequest convertRideToRideRequest(Ride ride){
        return modelMapper.map(ride,RideRequest.class);
    }
    public Ride convertRideRequestToRide(RideRequest rideRequest){
        return modelMapper.map(rideRequest,Ride.class);
    }
}
