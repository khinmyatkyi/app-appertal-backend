package com.example.backend.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.*;

import com.example.backend.enums.Status;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "lease")
@Getter
@Setter
public class Lease extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "lease_code", unique = true)
    private String leaseCode;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "deposit_amount")
    private BigDecimal depositAmount;

    @Column(name = "monthly_rent", nullable = false)
    private BigDecimal monthlyRent;

    @Column(name = "no_months", nullable = false)
    private Integer noMonths;

    @ManyToOne
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;

    @ManyToOne
    @JoinColumn(name = "apartment_id", nullable = false)
    private Apartment apartment;
    
    @Enumerated(EnumType.STRING)
    private Status status;

}