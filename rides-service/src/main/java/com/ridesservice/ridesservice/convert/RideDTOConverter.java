package com.ridesservice.ridesservice.convert;

import com.ridesservice.ridesservice.dto.request.RideRequest;
import com.ridesservice.ridesservice.dto.response.RideResponse;
import com.ridesservice.ridesservice.models.Ride;
import org.modelmapper.ModelMapper;
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
//    public PassengerRideResponse mapRideToPassengerRideResponse(Ride ride,
//                                                                List<StopResponse> stops,
//                                                                DriverResponse driver,
//                                                                CarResponse car) {
//        PassengerRideResponse passengerRideResponse = modelMapper.map(ride, PassengerRideResponse.class);
//        PromoCode promoCode = ride.getPromoCode();
//        if (promoCode != null) {
//            passengerRideResponse.setPromoCode(promoCode.getCode());
//        }
//        passengerRideResponse.setStatus(ride.getStatus().name());
//        passengerRideResponse.setStops(stops);
//        passengerRideResponse.setDriverName(driver.getFirstName());
//        passengerRideResponse.setDriverPhoneNumber(driver.getPhoneNumber());
//        passengerRideResponse.setDriverRating(driver.getRating());
//        passengerRideResponse.setCarColor(car.getColor());
//        passengerRideResponse.setCarMake(car.getCarMake());
//        passengerRideResponse.setCarNumber(car.getNumber());
//        return passengerRideResponse;
//    }
}
