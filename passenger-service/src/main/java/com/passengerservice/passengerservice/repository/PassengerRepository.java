package com.passengerservice.passengerservice.repository;

import com.passengerservice.passengerservice.models.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PassengerRepository extends JpaRepository<Passenger, Integer> {

    Optional<Passenger> findByName(String name);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
}
