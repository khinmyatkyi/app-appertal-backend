package com.example.backend.model;
import javax.persistence.*;

import com.example.backend.enums.ApartmentStatus;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "apartment")
@Getter
@Setter
public class Apartment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "apartment_code", unique = true)
    private String apartmentCode;
    
    @Column(name = "apartment_number", nullable = false)
    private String apartmentNumber;

    @Column(nullable = false)
    private String building;

    private String floor;

    private String type;

    @Column(name = "monthly_rent")
    private String monthlyRent;

    @Enumerated(EnumType.STRING)
    private ApartmentStatus status;
}