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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import static com.driverservice.driverservice.util.Messages.*;

@Service
@RequiredArgsConstructor
public class DriverServiceImpl implements DriverService {
    private final DriverRepository driverRepository;
    private final DriverDTOConverter driverDTOConverter;


    public DriverResponse getDriverById(Integer id){
        return driverDTOConverter.convertDriverToDriverResponse(driverRepository.findById(id)
                .orElseThrow(() -> new DriverNotFoundException(id)));
    }
    @Transactional
    public DriverResponse updateRating(Integer id, Double rating){
        if(!driverRepository.existsById(id))
            throw new DriverNotFoundException(id);
        validateRating(rating);
        Driver driver = getDriverEntityById(id);
        driver.setRating(rating);
        driverRepository.save(driver);
        return driverDTOConverter.convertDriverToDriverResponse(driver);
    }
    @Transactional
    public DriverResponse updateDriver(Integer id, DriverRequest driverRequest){
        if(!driverRepository.existsById(id))
            throw new DriverNotFoundException(id);
        Driver driver = driverDTOConverter.convertDriverRequestToDriver(driverRequest);
        emailUpdateCheck(id, driverRequest.getEmail());
        phoneUpdateCheck(id, driverRequest.getPhone());
        driver.setId(id);
        driverRepository.save(driver);
        return driverDTOConverter.convertDriverToDriverResponse(driver);
    }
    @Transactional
    public ResponseEntity<HttpStatus> deleteDriver(Integer id){
        Driver driver = driverRepository.findById(id).orElseThrow(() -> new DriverNotFoundException(id));
        driverRepository.delete(driver);
        return ResponseEntity.noContent().build();
    }
    @Transactional
    public DriverResponse addDriver(DriverRequest driverRequest){
        phoneIsUniqueCheck(driverRequest.getPhone());
        emailIsUniqueCheck(driverRequest.getEmail());
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
    private void emailUpdateCheck(Integer id, String email){
        Driver driver = getDriverEntityById(id);
        if(!driver.getEmail().equals(email))
            emailIsUniqueCheck(email);
    }
    private void phoneUpdateCheck(Integer id, String phone){
        Driver driver = getDriverEntityById(id);
        if(!driver.getPhone().equals(phone))
            phoneIsUniqueCheck(phone);
    }
    private void emailIsUniqueCheck(String email) {
        if (driverRepository.existsByEmail(email)) {
            throw new AlreadyExistsException(
                    String.format(DRIVER_WITH_EMAIL_EXISTS_MESSAGE, email));
        }
    }
    private void phoneIsUniqueCheck(String phone){
        if (driverRepository.existsByPhone(phone)) {
            throw new AlreadyExistsException(
                    String.format(DRIVER_WITH_PHONE_EXISTS_MESSAGE, phone));
        }
    }
    public DriversListResponse getDriversList(Integer offset, Integer page, String sortByField) {
        if (offset != null && page != null && sortByField != null) {
            validatePaginationParameters(offset, page);
            validateSortingParameter(sortByField);
            return getListWithPaginationAndSort(driverRepository.findAll(PageRequest.of(page, offset)
                            .withSort(Sort.by(sortByField)))
                    .map(driverDTOConverter::convertDriverToDriverResponse), sortByField);
        } else if (offset != null && page != null) {
            validatePaginationParameters(offset, page);
            return getListWithPagination(driverRepository.findAll(PageRequest.of(page, offset))
                    .map(driverDTOConverter::convertDriverToDriverResponse));
        } else if (sortByField != null) {
            validateSortingParameter(sortByField);
            return getListWithSort(driverRepository.findAll(Sort.by(sortByField))
                    .stream()
                    .map(driverDTOConverter::convertDriverToDriverResponse)
                    .toList(), sortByField);
        } else return getDrivers(driverRepository.findAll()
                .stream()
                .map(driverDTOConverter::convertDriverToDriverResponse)
                .toList());
    }
    public DriversListResponse getAvailableDriversList(Integer offset, Integer page,String sortByField) {
        if (offset != null && page != null && sortByField != null) {
            validatePaginationParameters(offset, page);
            validateSortingParameter(sortByField);
            return getListWithPaginationAndSort(driverRepository.findAllByIsAvailableIsTrue(PageRequest.of(page, offset)
                            .withSort(Sort.by(sortByField)))
                    .map(driverDTOConverter::convertDriverToDriverResponse), sortByField);
        } else if (offset != null && page != null) {
            validatePaginationParameters(offset, page);
            return getListWithPagination(driverRepository.findAllByIsAvailableIsTrue(PageRequest.of(page, offset))
                    .map(driverDTOConverter::convertDriverToDriverResponse));
        } else if (sortByField != null) {
            validateSortingParameter(sortByField);
            return getListWithSort(driverRepository.findAllByIsAvailableIsTrue(Sort.by(sortByField))
                    .stream()
                    .map(driverDTOConverter::convertDriverToDriverResponse)
                    .toList(), sortByField);
        } else return getDrivers(driverRepository.findAllByIsAvailableIsTrue()
                .stream()
                .map(driverDTOConverter::convertDriverToDriverResponse)
                .toList());
    }
    public DriversListResponse getDrivers(List<DriverResponse> drivers){
        return DriversListResponse.builder()
                .drivers(drivers)
                .total(drivers.size())
                .build();
    }
    private DriversListResponse getListWithPagination(Page<DriverResponse> responsePage) {
        return DriversListResponse.builder()
                .drivers(responsePage.getContent())
                .size(responsePage.getContent().size())
                .page(responsePage.getPageable().getPageNumber())
                .total((int) responsePage.getTotalElements())
                .build();
    }
    private DriversListResponse getListWithSort(List<DriverResponse> responseList, String sortByField) {
        return DriversListResponse.builder()
                .drivers(responseList)
                .sortedByField(sortByField)
                .total(responseList.size())
                .build();
    }
    private DriversListResponse getListWithPaginationAndSort(Page<DriverResponse> responsePage, String sortByField) {
        return DriversListResponse.builder()
                .drivers(responsePage.getContent())
                .size(responsePage.getContent().size())
                .page(responsePage.getPageable().getPageNumber())
                .total((int) responsePage.getTotalElements())
                .sortedByField(sortByField)
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
    private void validatePaginationParameters(Integer offset, Integer page) {
        if(offset <= 0) throw new InvalidRequestException("Offset parameter is invalid");
        if(page < 0)throw new InvalidRequestException("Page parameter is invalid");
    }
    private void validateRating(Double rating) {
        if(rating < 1)throw new InvalidRequestException("Rating parameter is invalid (Min value 1)");
        if(rating > 5)throw new InvalidRequestException("Page parameter is invalid (Max value 5)");
    }
}
