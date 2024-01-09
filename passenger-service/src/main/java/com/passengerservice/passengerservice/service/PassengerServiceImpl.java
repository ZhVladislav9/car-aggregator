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
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.lang.reflect.Field;

import static com.passengerservice.passengerservice.util.Messages.*;

@Service
@RequiredArgsConstructor
public class PassengerServiceImpl implements PassengerService {
    private final PassengerRepository passengerRepository;
    private final PassengerDTOConverter passengerDTOConverter;

    public PassengerResponse getPassengerById(int id){
        return passengerDTOConverter.convertPassengerToPassengerResponse(passengerRepository.findById(id)
                .orElseThrow(() -> new PassengerNotFoundException(id)));
    }

    @Transactional
    public PassengerResponse updatePassenger(int id, PassengerRequest passengerRequest){
        if(!passengerRepository.existsById(id))
            throw new PassengerNotFoundException(id);
        Passenger passenger = passengerDTOConverter.convertPassengerRequestToPassenger(passengerRequest);
        emailUpdateCheck(id, passenger.getEmail());
        phoneUpdateCheck(id, passenger.getPhone());
        passenger.setId(id);
        passengerRepository.save(passenger);
        return passengerDTOConverter.convertPassengerToPassengerResponse(passenger);
    }
    @Transactional
    public ResponseEntity<HttpStatus> deletePassenger(int id){
        Passenger passenger = passengerRepository
                .findById(id)
                .orElseThrow(() -> new PassengerNotFoundException(id));
        passengerRepository.delete(passenger);
        return ResponseEntity.noContent().build();
    }
    @Transactional
    public PassengerResponse addPassenger(PassengerRequest passengerRequest){
        emailIsUniqueCheck(passengerRequest.getEmail());
        phoneIsUniqueCheck(passengerRequest.getPhone());
        Passenger passenger = passengerDTOConverter.convertPassengerRequestToPassenger(passengerRequest);
        passengerRepository.save(passenger);
        return passengerDTOConverter.convertPassengerToPassengerResponse(passenger);
    }
    private void emailUpdateCheck(Integer id, String email){
        Passenger passenger = getPassengerEntityById(id);
        if(!passenger.getEmail().equals(email))
            emailIsUniqueCheck(email);
    }
    private void phoneUpdateCheck(Integer id, String phone){
        Passenger passenger = getPassengerEntityById(id);
        if(!passenger.getPhone().equals(phone))
            phoneIsUniqueCheck(phone);
    }
    private void emailIsUniqueCheck(String email) {
        if (passengerRepository.existsByEmail(email)) {
            throw new AlreadyExistsException(
                    String.format(PASSENGER_WITH_EMAIL_EXISTS_MESSAGE, email));
        }
    }
    private void phoneIsUniqueCheck(String phone){
        if (passengerRepository.existsByPhone(phone)) {
            throw new AlreadyExistsException(
                    String.format(PASSENGER_WITH_PHONE_EXISTS_MESSAGE, phone));
        }
    }
    private Passenger getPassengerEntityById(Integer id) {
        return passengerRepository.findById(id)
                .orElseThrow(() -> new PassengerNotFoundException(id));
    }
    public PassengersListResponse getPassengersList(Integer offset, Integer page, String sortByField) {
        if (offset != null && page != null && sortByField != null) {
            validatePaginationParameters(offset, page);
            validateSortingParameter(sortByField);
            return getListWithPaginationAndSort(passengerRepository.findAll(PageRequest.of(page, offset)
                            .withSort(Sort.by(sortByField)))
                    .map(passengerDTOConverter::convertPassengerToPassengerResponse), sortByField);
        } else if (offset != null && page != null) {
            validatePaginationParameters(offset, page);
            return getListWithPagination(passengerRepository.findAll(PageRequest.of(page, offset))
                    .map(passengerDTOConverter::convertPassengerToPassengerResponse));
        } else if (sortByField != null) {
            validateSortingParameter(sortByField);
            return getListWithSort(passengerRepository.findAll(Sort.by(sortByField))
                    .stream()
                    .map(passengerDTOConverter::convertPassengerToPassengerResponse)
                    .toList(), sortByField);
        } else return getPassengers(passengerRepository.findAll()
                .stream()
                .map(passengerDTOConverter::convertPassengerToPassengerResponse)
                .toList());
    }
    public PassengersListResponse getPassengers(List<PassengerResponse> passengers){
        return PassengersListResponse.builder()
                .passengers(passengers)
                .total(passengers.size())
                .build();
    }
    private PassengersListResponse getListWithPagination(Page<PassengerResponse> responsePage) {
        return PassengersListResponse.builder()
                .passengers(responsePage.getContent())
                .size(responsePage.getContent().size())
                .page(responsePage.getPageable().getPageNumber())
                .total((int) responsePage.getTotalElements())
                .build();
    }
    private PassengersListResponse getListWithSort(List<PassengerResponse> responseList, String sortByField) {
        return PassengersListResponse.builder()
                .passengers(responseList)
                .sortedByField(sortByField)
                .total(responseList.size())
                .build();
    }
    private PassengersListResponse getListWithPaginationAndSort(Page<PassengerResponse> responsePage, String sortByField) {
        return PassengersListResponse.builder()
                .passengers(responsePage.getContent())
                .size(responsePage.getContent().size())
                .page(responsePage.getPageable().getPageNumber())
                .total((int) responsePage.getTotalElements())
                .sortedByField(sortByField)
                .build();
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
