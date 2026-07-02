package com.example.backend.model;

import java.time.LocalDate;

import javax.persistence.*;

import com.example.backend.enums.MaintenanceRequestStatus;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "maintenance_request")
@Getter
@Setter
public class MaintenanceRequest extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "request_code", unique = true)
    private String requestCode;

    @Column(name = "request_date")
    private LocalDate requestDate;

    @Column(name = "assign_date")
    private LocalDate assignDate;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MaintenanceRequestStatus status;

    @ManyToOne
    @JoinColumn(name = "apartment_id", nullable = false)
    private Apartment apartment;

    @ManyToOne
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;
}
