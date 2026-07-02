package com.example.backend.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.example.backend.enums.MaintenanceRequestStatus;
import com.example.backend.model.MaintenanceRequest;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MaintenanceRequestDTO {

    private Long id;
    
    private String requestCode;

    private LocalDate requestDate;

    private LocalDate assignDate;

    private String description;

    private MaintenanceRequestStatus status;

    private Long apartmentId;
    private ApartmentDTO apartment;

    private Long tenantId;
    private TenantDTO tenant;

    private LocalDateTime createdAt;
    private String createdBy;

    private LocalDateTime updatedAt;
    private String updatedBy;

    public MaintenanceRequestDTO(MaintenanceRequest entity) {
        this.id = entity.getId();
        this.requestCode = entity.getRequestCode();
        this.requestDate = entity.getRequestDate();
        this.assignDate = entity.getAssignDate();
        this.description = entity.getDescription();
        this.status = entity.getStatus();

        this.apartmentId = entity.getApartment().getId();
        this.apartment = entity.getApartment() != null? new ApartmentDTO(entity.getApartment()) : null;
        this.tenantId = entity.getTenant().getId();
        this.tenant = entity.getTenant() != null ? new TenantDTO(entity.getTenant()) : null;

        this.createdAt = entity.getCreatedAt();
        this.createdBy = entity.getCreatedBy();
        this.updatedAt = entity.getUpdatedAt();
        this.updatedBy = entity.getUpdatedBy();
    }
}
