package com.passengerservice.passengerservice.service;

import com.passengerservice.passengerservice.convert.PassengerDTOConverter;
import com.passengerservice.passengerservice.dto.request.PassengerRequest;
import com.passengerservice.passengerservice.dto.response.PassengerResponse;
import com.passengerservice.passengerservice.dto.response.PassengersListResponse;
import com.passengerservice.passengerservice.exceptions.AlreadyExistsException;
import com.passengerservice.passengerservice.exceptions.InvalidRequestException;
import com.passengerservice.passengerservice.exceptions.PassengerNotFoundException;
import com.passengerservice.passengerservice.models.Passenger;
import com.passengerservice.passengerservice.repository.PassengerRepository;
import com.passengerservice.passengerservice.service.interfaces.PassengerService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.lang.reflect.Field;

import static com.passengerservice.passengerservice.util.Messages.*;

@Service
@Slf4j
public class PassengerServiceImpl implements PassengerService {
    @Autowired
    private PassengerRepository passengerRepository;
    @Autowired
    private PassengerDTOConverter passengerDTOConverter;

    public PassengersListResponse getPassengers(){
        List<PassengerResponse> passengers = passengerRepository.findAll()
                .stream()
                .map(passengerDTOConverter::convertPassengerToPassengerResponse)
                .toList();
        return PassengersListResponse.builder()
                .passengers(passengers)
                .total(passengers.size())
                .build();
    }
    public PassengerResponse getPassengerById(int id){
        return passengerDTOConverter.convertPassengerToPassengerResponse(passengerRepository.findById(id)
                .orElseThrow(() -> new PassengerNotFoundException(id)));
    }

    @Transactional
    public PassengerResponse updatePassenger(int id, PassengerRequest passengerRequest){
        Passenger editedPassenger = passengerRepository.findById(id)
                .orElseThrow(() -> new PassengerNotFoundException(id));
        Passenger passenger = passengerDTOConverter.convertPassengerRequestToPassenger(passengerRequest);

        if(passenger.getName() != null)
            editedPassenger.setName(passenger.getName());
        if(passenger.getSurname() != null)
            editedPassenger.setSurname(passenger.getSurname());
        if(passenger.getRating() != null)
            editedPassenger.setRating(passenger.getRating());
        if(passenger.getPhone() != null)
            editedPassenger.setPhone(passenger.getPhone());
        if(passenger.getEmail() != null)
            editedPassenger.setEmail(passenger.getEmail());
        passengerRepository.save(editedPassenger);
        return passengerDTOConverter.convertPassengerToPassengerResponse(editedPassenger);
    }
    @Transactional
    public ResponseEntity<HttpStatus> deletePassenger(int id){
        Passenger passenger = passengerRepository.findById(id).orElseThrow(() -> new PassengerNotFoundException(id));
        passengerRepository.delete(passenger);
        return ResponseEntity.noContent().build();
    }
    @Transactional
    public PassengerResponse addPassenger(PassengerRequest passengerRequest){
        dataIsUniqueCheck(passengerRequest);
        Passenger passenger = passengerDTOConverter.convertPassengerRequestToPassenger(passengerRequest);
        passengerRepository.save(passenger);
        return passengerDTOConverter.convertPassengerToPassengerResponse(passenger);
    }
    private PassengersListResponse getSortedPassengers(String sortByField) {
        List<PassengerResponse> responseList = passengerRepository.findAll(Sort.by(sortByField))
                .stream()
                .map(passengerDTOConverter::convertPassengerToPassengerResponse)
                .toList();
        return PassengersListResponse.builder()
                .passengers(responseList)
                .sortedByField(sortByField)
                .total(responseList.size())
                .build();
    }
    public PassengersListResponse getPassengersList(Integer offset, Integer page, String sortByField) {
        if (offset != null && page != null && sortByField != null) {
            validatePaginationParameters(offset, page);
            return getListWithPaginationAndSort(offset, page, sortByField);
        } else if (offset != null && page != null) {
            validatePaginationParameters(offset, page);
            return getListWithPagination(offset, page);
        } else if (sortByField != null) {
            validateSortingParameter(sortByField);
            return getSortedPassengers(sortByField);
        } else return getPassengers();
    }
    private PassengersListResponse getListWithPaginationAndSort(Integer offset, Integer page, String field) {
        Page<PassengerResponse> responsePage = passengerRepository.findAll(PageRequest.of(page, offset)
                        .withSort(Sort.by(field)))
                .map(passengerDTOConverter::convertPassengerToPassengerResponse);
        return PassengersListResponse.builder()
                .passengers(responsePage.getContent())
                .size(responsePage.getContent().size())
                .page(responsePage.getPageable().getPageNumber())
                .total((int) responsePage.getTotalElements())
                .sortedByField(field)
                .build();
    }
    private PassengersListResponse getListWithPagination(Integer offset, Integer page) {
        Page<PassengerResponse> responsePage = passengerRepository.findAll(PageRequest.of(page, offset))
                .map(passengerDTOConverter::convertPassengerToPassengerResponse);
        return PassengersListResponse.builder()
                .passengers(responsePage.getContent())
                .size(responsePage.getContent().size())
                .page(responsePage.getPageable().getPageNumber())
                .total((int) responsePage.getTotalElements())
                .build();
    }
    private void dataIsUniqueCheck(PassengerRequest request) {
        var errors = new HashMap<String, String>();

        if (passengerRepository.existsByEmail(request.getEmail())) {
            log.error("Passenger with email {} exists", request.getEmail());
            errors.put(
                    "email",
                    String.format(PASSENGER_WITH_EMAIL_EXISTS_MESSAGE, request.getEmail())
            );
        }
        if (passengerRepository.existsByPhone(request.getPhone())) {
            log.error("Passenger with phone {} exists", request.getPhone());
            errors.put(
                    "phone",
                    String.format(PASSENGER_WITH_PHONE_EXISTS_MESSAGE, request.getPhone())
            );
        }
        if (!errors.isEmpty()) {
            throw new AlreadyExistsException(errors);
        }
    }
    private void validateSortingParameter(String sortingParam) {
        List<String> fieldNames = Arrays.stream(PassengerResponse.class.getDeclaredFields())
                .map(Field::getName)
                .toList();

        if (!fieldNames.contains(sortingParam)) {
            String errorMessage = String.format(INVALID_SORTING_MESSAGE, fieldNames);
            throw new InvalidRequestException(errorMessage);
        }
    }
    private void validatePaginationParameters(Integer offset, Integer page) {
        if(offset < 0) throw new InvalidRequestException("Offset parameter is invalid");
        if(page < 0)throw new InvalidRequestException("Page parameter is invalid");
    }
}
