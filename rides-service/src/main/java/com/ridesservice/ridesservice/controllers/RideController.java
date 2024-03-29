package com.ridesservice.ridesservice.controllers;

import com.ridesservice.ridesservice.dto.request.RideRequest;
import com.ridesservice.ridesservice.dto.response.RideResponse;
import com.ridesservice.ridesservice.dto.response.RidesListResponse;
import com.ridesservice.ridesservice.service.RideServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api-v1/ride")
@RequiredArgsConstructor
public class RideController {
    private final RideServiceImpl rideServiceImpl;
    @GetMapping
    public RidesListResponse gerRides(@RequestParam(required = false) Integer offset,
                                      @RequestParam(required = false) Integer page,
                                      @RequestParam(required = false, name = "field") String sortByField){
        return rideServiceImpl.getRidesList(offset, page, sortByField);
    }
    @GetMapping("/{id}")
    public RideResponse getRideById(@PathVariable Integer id){
        return rideServiceImpl.getRideById(id);
    }

    @PutMapping("/{id}")
    public RideResponse updateRide(@PathVariable Integer id, @RequestBody @Valid RideRequest rideRequest){
        return rideServiceImpl.updateRide(id, rideRequest);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteRide(@PathVariable Integer id){
        return rideServiceImpl.deleteRide(id);
    }
    @PostMapping
    public RideResponse addRide(@RequestBody @Valid RideRequest rideRequest){
        return rideServiceImpl.addRide(rideRequest);
    }
    @PutMapping("/{id}/promo-code")
    public RideResponse enterPromoCode(@PathVariable Integer id, @RequestParam String promoCode){
        return rideServiceImpl.enterPromoCode(id, promoCode);
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
    @GetMapping("/passenger/{id}")
    public RidesListResponse getHistoryByPassengerId(
            @RequestParam(required = false) Integer offset,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false, name = "field") String sortByField,
            @PathVariable Integer  id){
        return rideServiceImpl.getPassengerRidesHistory(offset, page, sortByField, id);
    }
    @GetMapping("/driver/{id}")
    public RidesListResponse getHistoryByDriverId(
            @RequestParam(required = false) Integer offset,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false, name = "field") String sortByField,
            @PathVariable Integer  id){
        return rideServiceImpl.getDriverRidesHistory(offset, page, sortByField, id);
    }
}
