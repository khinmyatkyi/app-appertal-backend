package com.example.backend.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.example.backend.model.Tenant;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TenantDTO {

    private Long id;
    
    private String tenantCode;

    private String firstName;

    private String lastName;
    
    private String passportNumber;

    private String phone;

    private String email;

    private String address;

    private LocalDate dateOfBirth;

    private LocalDateTime createdAt;
    private String createdBy;

    private LocalDateTime updatedAt;
    private String updatedBy;

    public TenantDTO(Tenant entity) {
        this.id = entity.getId();
        this.tenantCode = entity.getTenantCode();
        this.firstName = entity.getFirstName();
        this.lastName = entity.getLastName();
        this.passportNumber = entity.getPassportNumber();
        this.phone = entity.getPhone();
        this.email = entity.getEmail();
        this.address = entity.getAddress();
        this.dateOfBirth = entity.getDateOfBirth();

        this.createdAt = entity.getCreatedAt();
        this.createdBy = entity.getCreatedBy();
        this.updatedAt = entity.getUpdatedAt();
        this.updatedBy = entity.getUpdatedBy();
    }
}
