package com.driverservice.driverservice.convert;

import com.driverservice.driverservice.dto.request.DriverRequest;
import com.driverservice.driverservice.dto.response.DriverResponse;
import com.driverservice.driverservice.models.Driver;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class DriverDTOConverter {
    private final ModelMapper modelMapper;

    public DriverResponse convertDriverToDriverResponse(Driver driver){
        return modelMapper.map(driver,DriverResponse.class);
    }
    public Driver convertDriverResponseToDriver(DriverResponse driverResponse){
        return modelMapper.map(driverResponse,Driver.class);
    }
    public DriverRequest convertDriverToDriverRequest(Driver driver){
        return modelMapper.map(driver,DriverRequest.class);
    }
    public Driver convertDriverRequestToDriver(DriverRequest driverRequest){
        return modelMapper.map(driverRequest,Driver.class);
    }
}
