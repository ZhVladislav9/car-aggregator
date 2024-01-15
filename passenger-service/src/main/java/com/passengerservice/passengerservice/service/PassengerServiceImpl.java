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
    public PassengerResponse updateRating(Integer id, Double rating){
        if(!passengerRepository.existsById(id))
            throw new PassengerNotFoundException(id);
        validateRating(rating);
        Passenger passenger = getPassengerEntityById(id);
        passenger.setRating(rating);
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
    @Transactional
    public PassengerResponse updatePassenger(int id, PassengerRequest passengerRequest){
        Passenger passenger = getPassengerEntityById(id);
        emailUpdateCheck(passengerRequest, passenger);
        phoneUpdateCheck(passengerRequest, passenger);
        passenger = passengerDTOConverter.convertPassengerRequestToPassenger(passengerRequest);
        passenger.setId(id);
        passengerRepository.save(passenger);
        return passengerDTOConverter.convertPassengerToPassengerResponse(passenger);
    }
    private void emailUpdateCheck(PassengerRequest passengerRequest, Passenger passenger){
        if(!passengerRequest.getEmail().equals(passenger.getEmail()))
            emailIsUniqueCheck(passengerRequest.getEmail());
    }
    private void phoneUpdateCheck(PassengerRequest passengerRequest, Passenger passenger){
        if(!passengerRequest.getPhone().equals(passenger.getPhone()))
            phoneIsUniqueCheck(passengerRequest.getEmail());
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
        if(offset <= 0) throw new InvalidRequestException("validation.passenger.offset.notValid");
        if(page < 0)throw new InvalidRequestException("validation.passenger.page.notValid");
    }
    private void validateRating(Double rating) {
        if(rating < 1)throw new InvalidRequestException("validation.passenger.rating.min = 1");
        if(rating > 5)throw new InvalidRequestException("validation.passenger.rating.max = 5");
    }
}
