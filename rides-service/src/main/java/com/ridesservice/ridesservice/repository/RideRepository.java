package com.ridesservice.ridesservice.repository;

import com.ridesservice.ridesservice.models.Ride;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface RideRepository extends JpaRepository<Ride,Integer> {
    List<Ride> findAllByPassengerId(Integer passengerId);
    Page<Ride> findAllByPassengerId(Integer passengerId, Pageable pageable);
    List<Ride> findAllByPassengerId(Integer passengerId, Sort sort);
    boolean existsByPassengerId(Integer passengerId);

}
