package com.ridesservice.ridesservice.repository;

import com.ridesservice.ridesservice.models.PromoCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PromoCodeRepository extends JpaRepository<PromoCode, Integer> {
    PromoCode findByName(String name);
    boolean existsByName(String name);
}
