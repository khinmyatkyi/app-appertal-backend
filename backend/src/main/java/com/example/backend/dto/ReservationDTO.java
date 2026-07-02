package com.example.backend.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.example.backend.enums.ReservationStatus;
import com.example.backend.model.Reservation;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReservationDTO {

    private Long id;

    private LocalDate reservationDate;

    private LocalDate moveInDate;

    private LocalDate moveOutDate;

    private ReservationStatus status;

    private Long apartmentId;
    private ApartmentDTO apartment;

    private Long tenantId;
    private TenantDTO tenant;
    
    private String notes;

    private LocalDateTime createdAt;
    private String createdBy;

    private LocalDateTime updatedAt;
    private String updatedBy;

    public ReservationDTO(Reservation entity) {
        this.id = entity.getId();
        this.reservationDate = entity.getReservationDate();
        this.moveInDate = entity.getMoveInDate();
        this.moveOutDate = entity.getMoveOutDate();
        this.status = entity.getStatus();

        this.apartmentId = entity.getApartment() != null ? entity.getApartment().getId() : null;
        this.apartment = entity.getApartment() != null ? new ApartmentDTO(entity.getApartment()) : null;
        
        this.tenantId = entity.getTenant() != null? entity.getTenant().getId() : null;
        this.tenant = entity.getTenant() != null? new TenantDTO(entity.getTenant()) : null;
        
        this.notes = entity.getNotes();

        this.createdAt = entity.getCreatedAt();
        this.createdBy = entity.getCreatedBy();
        this.updatedAt = entity.getUpdatedAt();
        this.updatedBy = entity.getUpdatedBy();
    }
}