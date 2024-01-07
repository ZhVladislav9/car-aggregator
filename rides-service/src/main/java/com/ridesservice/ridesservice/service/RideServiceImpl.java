package com.ridesservice.ridesservice.service;

import com.ridesservice.ridesservice.convert.RideDTOConverter;
import com.ridesservice.ridesservice.dto.request.RideRequest;
import com.ridesservice.ridesservice.dto.response.RideResponse;
import com.ridesservice.ridesservice.dto.response.RidesListResponse;
import com.ridesservice.ridesservice.exceptions.InvalidRequestException;
import com.ridesservice.ridesservice.exceptions.RideNotFoundException;
import com.ridesservice.ridesservice.models.Ride;
import com.ridesservice.ridesservice.models.RideStatus;
import com.ridesservice.ridesservice.repository.RideRepository;
import com.ridesservice.ridesservice.service.interfaces.RideService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static com.ridesservice.ridesservice.util.Messages.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class RideServiceImpl implements RideService {
    @Autowired
    private RideRepository rideRepository;
    @Autowired
    private RideDTOConverter rideDTOConverter;
    @Transactional
    public RideResponse addRide(RideRequest request) {
        //Ride ride = rideDTOConverter.convertRideRequestToRide(request);
        Ride ride = new Ride();
        ride.setPassengerId(request.getPassengerId());
        ride.setPickUpAddress(request.getPickUpAddress());
        ride.setDestinationAddress(request.getDestinationAddress());
        ride.setStartDate(LocalDateTime.now());
        ride.setPrice(calculateCost());
        ride.setStatus(RideStatus.CREATED);
        ride.setDriverId(getAvailableDriver());
        rideRepository.save(ride);
        return rideDTOConverter.convertRideToRideResponse(ride);
    }
    private Double calculateCost() {
        return Math.round(new Random().nextDouble(5, 20) * 100) / 100.0;
    }
    private Integer getAvailableDriver() {
        return new Random().nextInt(1, 10);
    }

    public RideResponse getRideById(Integer id) {
        return rideDTOConverter.convertRideToRideResponse(rideRepository.findById(id).orElseThrow());
    }

    public RideResponse acceptRide(Integer id){
        Ride ride = rideRepository.findById(id)
                .orElseThrow(() -> new RideNotFoundException(NOT_FOUND_WITH_ID_MESSAGE, id));
        if (ride.getStatus() != RideStatus.CREATED) {
            throw new InvalidRequestException("status is invalid");
        }
        ride.setStatus(RideStatus.ACCEPTED);
        ride.setFinishDate(LocalDateTime.now());
        rideRepository.save(ride);
        return rideDTOConverter.convertRideToRideResponse(ride);
    }
    public RideResponse finishRide(Integer id){
        Ride ride = rideRepository.findById(id)
                .orElseThrow(() -> new RideNotFoundException(NOT_FOUND_WITH_ID_MESSAGE, id));
        if (ride.getStatus() != RideStatus.ACCEPTED) {
            throw new InvalidRequestException("status is invalid");
        }
        ride.setStatus(RideStatus.FINISHED);
        ride.setFinishDate(LocalDateTime.now());
        rideRepository.save(ride);
        return rideDTOConverter.convertRideToRideResponse(ride);
    }
    public RideResponse rejectRide(Integer id){
        Ride ride = rideRepository.findById(id)
                .orElseThrow(() -> new RideNotFoundException(NOT_FOUND_WITH_ID_MESSAGE, id));
        if (ride.getStatus() != RideStatus.CREATED) {
            throw new InvalidRequestException("status is invalid");
        }
        ride.setStatus(RideStatus.REJECTED);
        ride.setFinishDate(LocalDateTime.now());
        rideRepository.save(ride);
        return rideDTOConverter.convertRideToRideResponse(ride);
    }

    @Transactional
    public RideResponse updatePrice(Integer id, Double price){
        Ride ride = rideRepository.findById(id)
                .orElseThrow(() -> new RideNotFoundException(NOT_FOUND_WITH_ID_MESSAGE, id));
        if(price != null){
            ride.setPrice(price);
        } else throw new InvalidRequestException("price is empty");

        rideRepository.save(ride);
        return rideDTOConverter.convertRideToRideResponse(ride);
    }
    @Transactional
    public RideResponse updateRide(Integer id, RideRequest rideRequest){
        Ride editedRide = rideRepository.findById(id)
                .orElseThrow(() -> new RideNotFoundException(NOT_FOUND_WITH_ID_MESSAGE, id));
        Ride ride = rideDTOConverter.convertRideRequestToRide(rideRequest);

        if(ride.getPickUpAddress() != null)
            editedRide.setPickUpAddress(ride.getPickUpAddress());
        if(ride.getDestinationAddress() != null)
            editedRide.setDestinationAddress(ride.getDestinationAddress());
        if(ride.getStartDate() != null)
            editedRide.setStartDate(ride.getStartDate());
        if(ride.getPrice() != null)
            editedRide.setPrice(ride.getPrice());
        if(ride.getPassengerId() != null)
            editedRide.setPassengerId(ride.getPassengerId());
        if(ride.getDriverId() != null)
            editedRide.setDriverId(ride.getDriverId());
        rideRepository.save(editedRide);
        return rideDTOConverter.convertRideToRideResponse(editedRide);
    }
    @Transactional
    public ResponseEntity<HttpStatus> deleteRide(Integer id){
        Ride ride = rideRepository.findById(id)
                .orElseThrow(() -> new RideNotFoundException(NOT_FOUND_WITH_ID_MESSAGE, id));
        rideRepository.delete(ride);
        return ResponseEntity.noContent().build();
    }
    public RidesListResponse getRidesList(Integer offset, Integer page, String sortByField) {
        if (offset != null && page != null && sortByField != null) {
            validatePaginationParameters(offset, page);
            return getListWithPaginationAndSort(rideRepository.findAll(PageRequest.of(page, offset)
                            .withSort(Sort.by(sortByField)))
                    .map(rideDTOConverter::convertRideToRideResponse), sortByField);
        } else if (offset != null && page != null) {
            validatePaginationParameters(offset, page);
            return getListWithPagination(rideRepository.findAll(PageRequest.of(page, offset))
                    .map(rideDTOConverter::convertRideToRideResponse));
        } else if (sortByField != null) {
            validateSortingParameter(sortByField);
            return getListWithSort(rideRepository.findAll(Sort.by(sortByField))
                    .stream()
                    .map(rideDTOConverter::convertRideToRideResponse)
                    .toList(), sortByField);
        } else return getRides(rideRepository.findAll()
                .stream()
                .map(rideDTOConverter::convertRideToRideResponse)
                .toList());
    }
    public RidesListResponse getPassengerRidesHistory(Integer offset, Integer page, String sortByField, Integer passengerId) {
        if(!rideRepository.existsByPassengerId(passengerId))
            throw new RideNotFoundException(NOT_FOUND_WITH_PASSENGER_ID_MESSAGE, passengerId);
        if (offset != null && page != null && sortByField != null) {
            validatePaginationParameters(offset, page);
            return getListWithPaginationAndSort(rideRepository.findAllByPassengerId(passengerId, PageRequest.of(page, offset)
                            .withSort(Sort.by(sortByField)))
                    .map(rideDTOConverter::convertRideToRideResponse), sortByField);
        } else if (offset != null && page != null) {
            validatePaginationParameters(offset, page);
            return getListWithPagination(rideRepository.findAllByPassengerId(passengerId, PageRequest.of(page, offset))
                    .map(rideDTOConverter::convertRideToRideResponse));
        } else if (sortByField != null) {
            validateSortingParameter(sortByField);
            return getListWithSort(rideRepository.findAllByPassengerId(passengerId, Sort.by(sortByField))
                    .stream()
                    .map(rideDTOConverter::convertRideToRideResponse)
                    .toList(), sortByField);
        } else {
            return getRides(rideRepository.findAllByPassengerId(passengerId)
                    .stream()
                    .map(rideDTOConverter::convertRideToRideResponse)
                    .toList());
        }
    }
    public RidesListResponse getDriverRidesHistory(Integer offset, Integer page, String sortByField, Integer driverId) {
        if(!rideRepository.existsByDriverId(driverId))
            throw new RideNotFoundException(NOT_FOUND_WITH_DRIVER_ID_MESSAGE, driverId);
        if (offset != null && page != null && sortByField != null) {
            validatePaginationParameters(offset, page);
            validateSortingParameter(sortByField);
            return getListWithPaginationAndSort(rideRepository.findAllByDriverId(driverId, PageRequest.of(page, offset)
                            .withSort(Sort.by(sortByField)))
                    .map(rideDTOConverter::convertRideToRideResponse), sortByField);
        } else if (offset != null && page != null) {
            validatePaginationParameters(offset, page);
            return getListWithPagination(rideRepository.findAllByDriverId(driverId, PageRequest.of(page, offset))
                    .map(rideDTOConverter::convertRideToRideResponse));
        } else if (sortByField != null) {
            validateSortingParameter(sortByField);
            return getListWithSort(rideRepository.findAllByDriverId(driverId, Sort.by(sortByField))
                    .stream()
                    .map(rideDTOConverter::convertRideToRideResponse)
                    .toList(), sortByField);
        } else {
            return getRides(rideRepository.findAllByDriverId(driverId)
                    .stream()
                    .map(rideDTOConverter::convertRideToRideResponse)
                    .toList());
        }
    }
    public RidesListResponse getRides(List<RideResponse> rides){
        return RidesListResponse.builder()
                .rides(rides)
                .total(rides.size())
                .build();
    }
    private RidesListResponse getListWithPagination(Page<RideResponse> responsePage) {
        return RidesListResponse.builder()
                .rides(responsePage.getContent())
                .size(responsePage.getContent().size())
                .page(responsePage.getPageable().getPageNumber())
                .total((int) responsePage.getTotalElements())
                .build();
    }
    private RidesListResponse getListWithSort(List<RideResponse> responseList, String sortByField) {
        return RidesListResponse.builder()
                .rides(responseList)
                .sortedByField(sortByField)
                .total(responseList.size())
                .build();
    }
    private RidesListResponse getListWithPaginationAndSort(Page<RideResponse> responsePage, String sortByField) {
        return RidesListResponse.builder()
                .rides(responsePage.getContent())
                .size(responsePage.getContent().size())
                .page(responsePage.getPageable().getPageNumber())
                .total((int) responsePage.getTotalElements())
                .sortedByField(sortByField)
                .build();
    }
    private void validateSortingParameter(String sortingParam) {
        List<String> fieldNames = Arrays.stream(RideResponse.class.getDeclaredFields())
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
