package com.ridesservice.ridesservice.controllers;

import com.ridesservice.ridesservice.dto.request.RideRequest;
import com.ridesservice.ridesservice.dto.response.RideResponse;
import com.ridesservice.ridesservice.dto.response.RidesListResponse;
import com.ridesservice.ridesservice.service.RideServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ride")
public class RideController {

    @Autowired
    private RideServiceImpl rideServiceImpl;
    @GetMapping("/all")
    public RidesListResponse gerRides(@RequestParam(required = false, name = "field") String sortByField){
        return rideServiceImpl.getRidesList(sortByField);
    }
    @GetMapping("/{id}")
    public RideResponse getRideById(@PathVariable Integer id){
        return rideServiceImpl.getRideById(id);
    }

    @PutMapping
    public RideResponse updateRide(@RequestParam Integer id, @RequestBody RideRequest rideRequest){
        return rideServiceImpl.updateRide(id, rideRequest);
    }
    @DeleteMapping
    public ResponseEntity<HttpStatus> deleteDriver(@RequestParam Integer id){
        return rideServiceImpl.deleteRide(id);
    }
    @PostMapping
    public RideResponse addRide(@RequestBody @Valid RideRequest rideRequest){
        return rideServiceImpl.addRide(rideRequest);
    }
}
