package com.paymentservice.paymentservice.repository;


import com.paymentservice.paymentservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<User,Long> {
}