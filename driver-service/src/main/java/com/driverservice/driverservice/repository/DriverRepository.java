package com.driverservice.driverservice.repository;

import com.driverservice.driverservice.models.Driver;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Integer> {


    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    List<Driver> findAllByIsAvailableIsTrue();
    List<Driver> findAllByIsAvailableIsTrue(Sort sort);
    Page<Driver> findAllByIsAvailableIsTrue(Pageable pageable);

}
