package com.example.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.example.backend.model.Lease;
import com.example.backend.model.Tenant;
import com.example.backend.dto.TenantDTO;
import com.example.backend.enums.Status;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LeaseDTO {

    private Long id;
    
    private String leaseCode;

    private LocalDate startDate;

    private LocalDate endDate;

    private BigDecimal depositAmount;

    private BigDecimal monthlyRent;

    private Integer noMonths;

    private Long tenantId;
    private TenantDTO tenant;

    private Long apartmentId;
    private ApartmentDTO apartment;
    
    private Status status;

    private LocalDateTime createdAt;
    private String createdBy;

    private LocalDateTime updatedAt;
    private String updatedBy;

    public LeaseDTO(Lease entity) {
        this.id = entity.getId();
        this.leaseCode = entity.getLeaseCode();
        this.startDate = entity.getStartDate();
        this.endDate = entity.getEndDate();
        this.depositAmount = entity.getDepositAmount();
        this.monthlyRent = entity.getMonthlyRent();
        this.noMonths = entity.getNoMonths();

        this.tenantId = entity.getTenant().getId();
        this.tenant = entity.getTenant() != null ? new TenantDTO(entity.getTenant()) : null;
        
        this.apartmentId = entity.getApartment().getId();
        this.apartment = entity.getApartment() != null ? new ApartmentDTO(entity.getApartment()) : null;
        
        this.status = entity.getStatus();

        this.createdAt = entity.getCreatedAt();
        this.createdBy = entity.getCreatedBy();
        this.updatedAt = entity.getUpdatedAt();
        this.updatedBy = entity.getUpdatedBy();
    }
}
