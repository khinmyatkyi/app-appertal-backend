package com.example.backend.dto;

import java.math.BigDecimal;
import com.example.backend.enums.ItemType;
import com.example.backend.model.InvoiceDetail;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class InvoiceDetailDTO {

    private Long id;

    private Long invoiceId;

    private ItemType itemType;

    private Integer quantity;

    private BigDecimal unitPrice;

    private String remarks;

    public InvoiceDetailDTO(InvoiceDetail entity) {
        this.id = entity.getId();
        this.invoiceId = entity.getInvoice().getId();
        this.itemType = entity.getItemType();
        this.quantity = entity.getQuantity();
        this.unitPrice = entity.getUnitPrice();
        this.remarks = entity.getRemarks();
    }
}
