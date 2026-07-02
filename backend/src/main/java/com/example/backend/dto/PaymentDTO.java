package com.example.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.example.backend.enums.PaymentMethod;
import com.example.backend.enums.PaymentType;
import com.example.backend.enums.Status;
import com.example.backend.model.Invoice;
import com.example.backend.model.Payment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PaymentDTO {

    private Long id;
    
    private String paymentCode;

    private LocalDate paymentDate;

    private BigDecimal amount;

    private PaymentMethod paymentMethod;
    
    private PaymentType paymentType;

    private Long leaseId;
    private LeaseDTO lease;
    
    private Long invoiceId;
    private InvoiceDTO invoice;
    
    private Status status;

    private LocalDateTime createdAt;
    private String createdBy;

    private LocalDateTime updatedAt;
    private String updatedBy;

    public PaymentDTO(Payment entity) {
        this.id = entity.getId();
        this.paymentCode = entity.getPaymentCode();
        this.paymentDate = entity.getPaymentDate();
        this.amount = entity.getAmount();
        this.paymentMethod = entity.getPaymentMethod();
        this.paymentType = entity.getPaymentType();

        this.leaseId = entity.getLease().getId();
        this.lease = entity.getLease() != null? new LeaseDTO(entity.getLease()) : null;
        
        this.invoiceId = entity.getInvoice() != null? entity.getInvoice().getId() : null;
        this.invoice = entity.getInvoice() != null? new InvoiceDTO(entity.getInvoice()) : null;
        
        this.status = entity.getStatus();

        this.createdAt = entity.getCreatedAt();
        this.createdBy = entity.getCreatedBy();
        this.updatedAt = entity.getUpdatedAt();
        this.updatedBy = entity.getUpdatedBy();
    }
}