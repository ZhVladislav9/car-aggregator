package com.driverservice.driverservice.service;

import com.driverservice.driverservice.convert.DriverDTOConverter;
import com.driverservice.driverservice.dto.request.DriverRequest;
import com.driverservice.driverservice.dto.response.DriverResponse;
import com.driverservice.driverservice.dto.response.DriversListResponse;
import com.driverservice.driverservice.exceptions.AlreadyExistsException;
import com.driverservice.driverservice.exceptions.DriverNotFoundException;
import com.driverservice.driverservice.exceptions.InvalidRequestException;
import com.driverservice.driverservice.models.Driver;
import com.driverservice.driverservice.repository.DriverRepository;
import com.driverservice.driverservice.service.interfaces.DriverService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static com.driverservice.driverservice.util.Messages.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class DriverServiceImpl implements DriverService {
    @Autowired
    private DriverRepository driverRepository;
    @Autowired
    private DriverDTOConverter driverDTOConverter;

    public DriversListResponse getDrivers(){
        List<DriverResponse> drivers = driverRepository.findAll()
                .stream()
                .map(driverDTOConverter::convertDriverToDriverResponse)
                .toList();
        return DriversListResponse.builder()
                .drivers(drivers)
                .total(drivers.size())
                .build();
    }
    public DriverResponse getDriverById(Integer id){
        return driverDTOConverter.convertDriverToDriverResponse(driverRepository.findById(id)
                .orElseThrow(() -> new DriverNotFoundException(id)));
    }

    @Transactional
    public DriverResponse updateDriver(Integer id, DriverRequest driverRequest){
        Driver editedDriver = driverRepository.findById(id)
                .orElseThrow(() -> new DriverNotFoundException(id));
        Driver driver = driverDTOConverter.convertDriverRequestToDriver(driverRequest);

        if(driver.getName() != null)
            editedDriver.setName(driver.getName());
        if(driver.getSurname() != null)
            editedDriver.setSurname(driver.getSurname());
        if(driver.getRating() != null)
            editedDriver.setRating(driver.getRating());
        if(driver.getPhone() != null)
            editedDriver.setPhone(driver.getPhone());
        if(driver.getIsAvailable() != null)
            editedDriver.setIsAvailable(driver.getIsAvailable());
        driverRepository.save(editedDriver);
        return driverDTOConverter.convertDriverToDriverResponse(editedDriver);
    }
    @Transactional
    public ResponseEntity<HttpStatus> deleteDriver(Integer id){
        Driver driver = driverRepository.findById(id).orElseThrow(() -> new DriverNotFoundException(id));
        driverRepository.delete(driver);
        return ResponseEntity.noContent().build();
    }
    @Transactional
    public DriverResponse addDriver(DriverRequest driverRequest){
        dataIsUniqueCheck(driverRequest);
        Driver driver = driverDTOConverter.convertDriverRequestToDriver(driverRequest);
        driverRepository.save(driver);
        return driverDTOConverter.convertDriverToDriverResponse(driver);
    }
    private Driver getDriverEntityById(Integer id) {
        return driverRepository.findById(id)
                .orElseThrow(() -> new DriverNotFoundException(id));
    }
    public DriverResponse changeAvailability(Integer id) {
        Driver driver = getDriverEntityById(id);
        driver.setIsAvailable(!driver.getIsAvailable());
        driverRepository.save(driver);
        return driverDTOConverter.convertDriverToDriverResponse(driver);
    }

    private DriversListResponse getSortedDrivers(String sortByField) {
        List<DriverResponse> responseList = driverRepository.findAll(Sort.by(sortByField))
                .stream()
                .map(driverDTOConverter::convertDriverToDriverResponse)
                .toList();
        return DriversListResponse.builder()
                .drivers(responseList)
                .sortedByField(sortByField)
                .build();
    }
    public DriversListResponse getDriversList(String sortByField) {

        if (sortByField != null) {
            validateSortingParameter(sortByField);
            return getSortedDrivers(sortByField);
        } else return getDrivers();
    }
    private void dataIsUniqueCheck(DriverRequest request) {
        var errors = new HashMap<String, String>();

        if (driverRepository.existsByPhone(request.getPhone())) {
            log.error("Driver with phone {} is exists", request.getPhone());
            errors.put(
                    "phone",
                    String.format(DRIVER_WITH_PHONE_S_ALREADY_EXISTS_MESSAGE, request.getPhone())
            );
        }
        if (!errors.isEmpty()) {
            throw new AlreadyExistsException(errors);
        }
    }
    public DriversListResponse getAvailableDriversList(String sortByField) {
        if (sortByField != null) {
            return getAvailableSortedDrivers(sortByField);
        } else return getAvailableDrivers();
    }
    public DriversListResponse getAvailableDrivers() {
        List<DriverResponse> drivers = driverRepository.findAllByIsAvailableIsTrue()
                .stream()
                .map(driverDTOConverter::convertDriverToDriverResponse)
                .toList();
        return DriversListResponse.builder()
                .drivers(drivers)
                .total(drivers.size())
                .build();
    }
    public DriversListResponse getAvailableSortedDrivers(String field) {
        List<DriverResponse> responseList = driverRepository.findAllByIsAvailableIsTrue(Sort.by(field))
                .stream()
                .map(driverDTOConverter::convertDriverToDriverResponse)
                .toList();
        return DriversListResponse.builder()
                .drivers(responseList)
                .sortedByField(field)
                .build();
    }
    private void validateSortingParameter(String sortingParam) {
        List<String> fieldNames = Arrays.stream(DriverResponse.class.getDeclaredFields())
                .map(Field::getName)
                .toList();

        if (!fieldNames.contains(sortingParam)) {
            String errorMessage = String.format(INVALID_SORTING_MESSAGE, fieldNames);
            throw new InvalidRequestException(errorMessage);
        }
    }
}
