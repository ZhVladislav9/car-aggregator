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
@RequestMapping("/driver")
@RequiredArgsConstructor
public class DriverController {
    private final DriverServiceImpl driverServiceImpl;
    @GetMapping("/all")
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
    @PutMapping
    public DriverResponse updateDriver(@RequestParam Integer id, @RequestBody @Valid DriverRequest driverRequest){
        return driverServiceImpl.updateDriver(id, driverRequest);
    }
    @DeleteMapping
    public ResponseEntity<HttpStatus> deleteDriver(@RequestParam Integer id){
        return driverServiceImpl.deleteDriver(id);
    }
    @PostMapping
    public DriverResponse addDriver(@RequestBody @Valid DriverRequest driverRequest){
        return driverServiceImpl.addDriver(driverRequest);
    }

    @PutMapping("/{id}")
    public DriverResponse changeAvailability(@PathVariable Integer id) {
        return driverServiceImpl.changeAvailability(id);
    }
}
