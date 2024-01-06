package com.ridesservice.ridesservice.service.interfaces;

import com.ridesservice.ridesservice.dto.request.RideRequest;
import com.ridesservice.ridesservice.dto.response.RideResponse;
import com.ridesservice.ridesservice.dto.response.RidesListResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface RideService {
    public RideResponse addRide(RideRequest request);
    public RideResponse getRideById(Integer id);
    public RideResponse updateRide(Integer id, RideRequest rideRequest);
    public ResponseEntity<HttpStatus> deleteRide(Integer id);
    public RidesListResponse getRidesList(Integer offset, Integer page, String sortByField);
}
