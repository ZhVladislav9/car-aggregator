package com.passengerservice.passengerservice.controllers;

import com.passengerservice.passengerservice.dto.request.PassengerRequest;
import com.passengerservice.passengerservice.dto.response.PassengerResponse;
import com.passengerservice.passengerservice.dto.response.PassengersListResponse;
import com.passengerservice.passengerservice.service.PassengerServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/passenger")
public class PassengerController {
    @Autowired
    private PassengerServiceImpl passengerServiceImpl;

    @GetMapping("/all")
    public PassengersListResponse getPassengers(@RequestParam(required = false, name = "field") String sortByField) {
        return passengerServiceImpl.getPassengersList(sortByField);
    }
    @GetMapping("/{id}")
    public PassengerResponse getPassengerById(@PathVariable int id){
        return passengerServiceImpl.getPassengerById(id);
    }
    @PutMapping
    public PassengerResponse updatePassenger(@RequestParam int id, @RequestBody PassengerRequest passengerRequest){
        return passengerServiceImpl.updatePassenger(id, passengerRequest);
    }
    @DeleteMapping
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public ResponseEntity<HttpStatus> deletePassenger(@RequestParam int id){
        return passengerServiceImpl.deletePassenger(id);
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public PassengerResponse addPassenger(@RequestBody @Valid PassengerRequest passengerRequest){
        return passengerServiceImpl.addPassenger(passengerRequest);
    }
}
