package com.example.backend.dto;

import java.time.LocalDateTime;

import com.example.backend.enums.ApartmentStatus;
import com.example.backend.model.Apartment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ApartmentDTO {

    private Long id;
    
    private String apartmentCode;

    private String apartmentNumber;

    private String building;

    private String floor;

    private String type;

    private String monthlyRent;

    private ApartmentStatus status;

    private LocalDateTime createdAt;
    private String createdBy;

    private LocalDateTime updatedAt;
    private String updatedBy;

    public ApartmentDTO(Apartment entity) {
        this.id = entity.getId();
        this.apartmentCode = entity.getApartmentCode();
        this.apartmentNumber = entity.getApartmentNumber();
        this.building = entity.getBuilding();
        this.floor = entity.getFloor();
        this.type = entity.getType();
        this.monthlyRent = entity.getMonthlyRent();
        this.status = entity.getStatus();

        this.createdAt = entity.getCreatedAt();
        this.createdBy = entity.getCreatedBy();

        this.updatedAt = entity.getUpdatedAt();
        this.updatedBy = entity.getUpdatedBy();
    }
}
