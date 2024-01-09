package com.passengerservice.passengerservice.repository;

import com.passengerservice.passengerservice.models.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PassengerRepository extends JpaRepository<Passenger, Integer> {

    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
}
