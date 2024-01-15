package com.passengerservice.passengerservice.controllers;

import com.passengerservice.passengerservice.dto.request.PassengerRequest;
import com.passengerservice.passengerservice.dto.response.PassengerResponse;
import com.passengerservice.passengerservice.dto.response.PassengersListResponse;
import com.passengerservice.passengerservice.service.PassengerServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api-v1/passenger")
@RequiredArgsConstructor
public class PassengerController {
    private final PassengerServiceImpl passengerServiceImpl;

    @GetMapping
    public PassengersListResponse getPassengers(
            @RequestParam(required = false) Integer offset,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false, name = "field") String sortByField) {
        return passengerServiceImpl.getPassengersList(offset, page, sortByField);
    }
    @GetMapping("/{id}")
    public PassengerResponse getPassengerById(@PathVariable int id){
        return passengerServiceImpl.getPassengerById(id);
    }
    @PutMapping("/{id}")
    public PassengerResponse updatePassenger(@PathVariable int id, @RequestBody @Valid PassengerRequest passengerRequest){
        return passengerServiceImpl.updatePassenger(id, passengerRequest);
    }
    @PutMapping("/{id}/rating")
    public PassengerResponse updateRating(@PathVariable Integer id, @RequestParam Double rating){
        return passengerServiceImpl.updateRating(id, rating);
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public ResponseEntity<HttpStatus> deletePassenger(@PathVariable int id){
        return passengerServiceImpl.deletePassenger(id);
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public PassengerResponse addPassenger(@RequestBody @Valid PassengerRequest passengerRequest){
        return passengerServiceImpl.addPassenger(passengerRequest);
    }
}
