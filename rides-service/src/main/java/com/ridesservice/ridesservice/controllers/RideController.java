package com.ridesservice.ridesservice.controllers;

import com.ridesservice.ridesservice.dto.request.RideRequest;
import com.ridesservice.ridesservice.dto.response.RideResponse;
import com.ridesservice.ridesservice.dto.response.RidesListResponse;
import com.ridesservice.ridesservice.service.RideServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ride")
@RequiredArgsConstructor
public class RideController {
    private final RideServiceImpl rideServiceImpl;
    @GetMapping("/all")
    public RidesListResponse gerRides(@RequestParam(required = false) Integer offset,
                                      @RequestParam(required = false) Integer page,
                                      @RequestParam(required = false, name = "field") String sortByField){
        return rideServiceImpl.getRidesList(offset, page, sortByField);
    }
    @GetMapping("/{id}")
    public RideResponse getRideById(@PathVariable Integer id){
        return rideServiceImpl.getRideById(id);
    }

    @PutMapping
    public RideResponse updateRide(@RequestParam Integer id, @RequestBody @Valid RideRequest rideRequest){
        return rideServiceImpl.updateRide(id, rideRequest);
    }
    @DeleteMapping
    public ResponseEntity<HttpStatus> deleteRide(@RequestParam Integer id){
        return rideServiceImpl.deleteRide(id);
    }
    @PostMapping
    public RideResponse addRide(@RequestBody @Valid RideRequest rideRequest){
        return rideServiceImpl.addRide(rideRequest);
    }
    @PutMapping("/{id}/promo-code")
    public RideResponse enterPromoCode(@PathVariable Integer id, @RequestParam(name = "promoCode") String promoCodeName){
        return rideServiceImpl.enterPromoCode(id, promoCodeName);
    }
    @PutMapping("/{id}/finish")
    public RideResponse finishRide(@PathVariable Integer id){
        return rideServiceImpl.finishRide(id);
    }
    @PutMapping("/{id}/reject")
    public RideResponse rejectRide(@PathVariable Integer id){
        return rideServiceImpl.rejectRide(id);
    }
    @PutMapping("/{id}/accept")
    public RideResponse acceptRide(@PathVariable Integer id){
        return rideServiceImpl.acceptRide(id);
    }
    @PutMapping("/{id}/update-price")
    public RideResponse updatePrice(@PathVariable Integer id,@RequestParam Double price){
        return rideServiceImpl.updatePrice(id, price);
    }
    @GetMapping("/passenger/{id}/history")
    public RidesListResponse getHistoryByPassengerId(
            @RequestParam(required = false) Integer offset,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false, name = "field") String sortByField,
            @PathVariable Integer  id){
        return rideServiceImpl.getPassengerRidesHistory(offset, page, sortByField, id);
    }
    @GetMapping("/driver/{id}/history")
    public RidesListResponse getHistoryByDriverId(
            @RequestParam(required = false) Integer offset,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false, name = "field") String sortByField,
            @PathVariable Integer  id){
        return rideServiceImpl.getDriverRidesHistory(offset, page, sortByField, id);
    }
}
