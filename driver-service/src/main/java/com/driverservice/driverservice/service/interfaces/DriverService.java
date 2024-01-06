package com.driverservice.driverservice.service.interfaces;

import com.driverservice.driverservice.dto.request.DriverRequest;
import com.driverservice.driverservice.dto.response.DriverResponse;
import com.driverservice.driverservice.dto.response.DriversListResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface DriverService {
    public DriversListResponse getDrivers();
    public DriverResponse getDriverById(Integer id);
    public DriverResponse updateDriver(Integer id, DriverRequest DriverRequest);
    public ResponseEntity<HttpStatus> deleteDriver(Integer id);
    public DriverResponse addDriver(DriverRequest DriverRequest);
}
