package com.ridesservice.ridesservice.util;

import com.ridesservice.ridesservice.models.Passenger;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "productsBlocking", url = "http://localhost:8094/api-v1/passenger")
public interface RidesFeignClient {
    @GetMapping("/{id}")
    Passenger getPassenger(@PathVariable Integer id);
}
