package com.driverservice.driverservice.controllers;

import com.driverservice.driverservice.dto.request.DriverRequest;
import com.driverservice.driverservice.dto.response.DriverResponse;
import com.driverservice.driverservice.dto.response.DriversListResponse;
import com.driverservice.driverservice.service.DriverServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api-v1/driver")
@RequiredArgsConstructor
public class DriverController {
    private final DriverServiceImpl driverServiceImpl;
    @GetMapping
    public DriversListResponse gerDrivers(
            @RequestParam(required = false) Integer offset,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false, name = "field") String sortByField){
        return driverServiceImpl.getDriversList(offset, page, sortByField);
    }
    @GetMapping("/{id}")
    public DriverResponse getDriverById(@PathVariable Integer id){
        return driverServiceImpl.getDriverById(id);
    }
    @GetMapping("/available")
    public DriversListResponse getAvailableDrivers(@RequestParam(required = false) Integer offset,
                                                   @RequestParam(required = false) Integer page,
                                                   @RequestParam(required = false, name = "field") String sortByField) {
        return driverServiceImpl.getAvailableDriversList(offset, page, sortByField);
    }
    @PutMapping("/{id}")
    public DriverResponse updateDriver(@PathVariable Integer id, @RequestBody @Valid DriverRequest driverRequest){
        return driverServiceImpl.updateDriver(id, driverRequest);
    }
    @PutMapping("/{id}/rating")
    public DriverResponse updateRating(@PathVariable Integer id, @RequestParam Double rating){
        return driverServiceImpl.updateRating(id, rating);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteDriver(@PathVariable Integer id){
        return driverServiceImpl.deleteDriver(id);
    }
    @PostMapping
    public DriverResponse addDriver(@RequestBody @Valid DriverRequest driverRequest){
        return driverServiceImpl.addDriver(driverRequest);
    }

    @PutMapping("/{id}/availability")
    public DriverResponse changeAvailability(@PathVariable Integer id) {
        return driverServiceImpl.changeAvailability(id);
    }
}
