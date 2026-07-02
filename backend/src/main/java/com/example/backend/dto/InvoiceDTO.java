package com.example.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.backend.enums.InvoiceStatus;
import com.example.backend.enums.InvoiceType;
import com.example.backend.model.Invoice;
import com.example.backend.model.InvoiceDetail;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class InvoiceDTO {

    private Long id;

    private String invoiceNo;

    private Long leaseId;
    private LeaseDTO lease;

    private Long tenantId;
    private TenantDTO tenant;

    private InvoiceType invoiceType;

    private LocalDate billingPeriodStart;

    private LocalDate billingPeriodEnd;

    private LocalDate issueDate;

    private LocalDate dueDate;
    
    private List<InvoiceDetailDTO> invoiceDetails = new ArrayList<>();

    private BigDecimal subtotal;

    private BigDecimal discount;

    private BigDecimal taxAmount;

    private BigDecimal totalAmount;

    private BigDecimal paidAmount;

    private InvoiceStatus status;

    private String notes;

    private LocalDateTime createdAt;
    private String createdBy;

    private LocalDateTime updatedAt;
    private String updatedBy;

    public InvoiceDTO(Invoice entity) {
        this.id = entity.getId();
        this.invoiceNo = entity.getInvoiceNo();
        this.leaseId = entity.getLease() != null ? entity.getLease().getId() : null;
        this.lease = entity.getLease() != null ? new LeaseDTO(entity.getLease()) : null;
        
        this.tenantId = entity.getTenant() != null ? entity.getTenant().getId() : null;
        this.tenant = entity.getTenant() != null ? new TenantDTO(entity.getTenant()) : null;
        
        this.invoiceType = entity.getInvoiceType();
        this.billingPeriodStart = entity.getBillingPeriodStart();
        this.billingPeriodEnd = entity.getBillingPeriodEnd();
        this.issueDate = entity.getIssueDate();
        this.dueDate = entity.getDueDate();
        this.subtotal = entity.getSubtotal();
        this.discount = entity.getDiscount();
        this.taxAmount = entity.getTaxAmount();
        this.totalAmount = entity.getTotalAmount();
        this.paidAmount = entity.getPaidAmount();
        this.status = entity.getStatus();
        this.notes = entity.getNotes();

        this.createdAt = entity.getCreatedAt();
        this.createdBy = entity.getCreatedBy();

        this.updatedAt = entity.getUpdatedAt();
        this.updatedBy = entity.getUpdatedBy();
    }
}
