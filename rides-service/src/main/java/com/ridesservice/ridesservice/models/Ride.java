package com.ridesservice.ridesservice.models;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "rides")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Ride {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @Column(name = "pick_up_address",nullable = false)
    String pickUpAddress;
    @Column(name = "destination_address",nullable = false)
    String destinationAddress;
    Double price;
    @Column(name = "passenger_id",nullable = false)
    Integer passengerId;
    @Column(name = "driver_id",nullable = false)
    Integer driverId;
    LocalDateTime date;
    @Enumerated(EnumType.STRING)
    private RideStatus status;
}
