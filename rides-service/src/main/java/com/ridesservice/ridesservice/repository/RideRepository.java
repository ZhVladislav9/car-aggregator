package com.ridesservice.ridesservice.repository;

import com.ridesservice.ridesservice.models.Ride;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RideRepository extends JpaRepository<Ride,Integer> {

}
