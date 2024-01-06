package com.ridesservice.ridesservice.service;

import com.ridesservice.ridesservice.convert.RideDTOConverter;
import com.ridesservice.ridesservice.dto.request.RideRequest;
import com.ridesservice.ridesservice.dto.response.RideResponse;
import com.ridesservice.ridesservice.dto.response.RidesListResponse;
import com.ridesservice.ridesservice.exceptions.RideNotFoundException;
import com.ridesservice.ridesservice.models.Ride;
import com.ridesservice.ridesservice.repository.RideRepository;
import com.ridesservice.ridesservice.service.interfaces.RideService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class RideServiceImpl implements RideService {
    @Autowired
    private RideRepository rideRepository;
    @Autowired
    private RideDTOConverter rideDTOConverter;

    @Transactional
    public RideResponse addRide(RideRequest request) {
        Ride ride = rideDTOConverter.convertRideRequestToRide(request);
        ride.setDate(LocalDateTime.now());
        rideRepository.save(ride);
        return rideDTOConverter.convertRideToRideResponse(ride);
    }

    public RideResponse getRideById(Integer id) {
        return rideDTOConverter.convertRideToRideResponse(rideRepository.findById(id).orElseThrow());
    }
    @Transactional
    public RideResponse updateRide(Integer id, RideRequest rideRequest){
        Ride editedRide = rideRepository.findById(id)
                .orElseThrow(() -> new RideNotFoundException(id));
        Ride ride = rideDTOConverter.convertRideRequestToRide(rideRequest);

        if(ride.getPickUpAddress() != null)
            editedRide.setPickUpAddress(ride.getPickUpAddress());
        if(ride.getDestinationAddress() != null)
            editedRide.setDestinationAddress(ride.getDestinationAddress());
        if(ride.getDate() != null)
            editedRide.setDate(ride.getDate());
        if(ride.getPrice() != null)
            editedRide.setPrice(ride.getPrice());
        if(ride.getPassengerId() != null)
            editedRide.setPassengerId(ride.getPassengerId());
        if(ride.getDriverId() != null)
            editedRide.setDriverId(ride.getDriverId());
        rideRepository.save(editedRide);
        return rideDTOConverter.convertRideToRideResponse(editedRide);
    }
    @Transactional
    public ResponseEntity<HttpStatus> deleteRide(Integer id){
        Ride ride = rideRepository.findById(id).orElseThrow(() -> new RideNotFoundException(id));
        rideRepository.delete(ride);
        return ResponseEntity.noContent().build();
    }
    public RidesListResponse getRides(){
        List<RideResponse> rides = rideRepository.findAll()
                .stream()
                .map(rideDTOConverter::convertRideToRideResponse)
                .toList();
        return RidesListResponse.builder()
                .rides(rides)
                .total(rides.size())
                .build();
    }
    private RidesListResponse getSortedRides(String sortByField) {
        List<RideResponse> responseList = rideRepository.findAll(Sort.by(sortByField))
                .stream()
                .map(rideDTOConverter::convertRideToRideResponse)
                .toList();
        return RidesListResponse.builder()
                .rides(responseList)
                .sortedByField(sortByField)
                .build();
    }
    public RidesListResponse getRidesList(String sortByField) {
        if (sortByField != null) {
            return getSortedRides(sortByField);
        } else return getRides();
    }
}
