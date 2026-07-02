package com.example.backend.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.backend.dto.InvoiceDTO;
import com.example.backend.dto.InvoiceDetailDTO;
import com.example.backend.enums.InvoiceStatus;
import com.example.backend.exception.CustomException;
import com.example.backend.model.Invoice;
import com.example.backend.model.InvoiceDetail;
import com.example.backend.model.Lease;
import com.example.backend.model.Tenant;
import com.example.backend.repository.InvoiceRepository;
import com.example.backend.repository.LeaseRepository;
import com.example.backend.repository.TenantRepository;

@Service
public class InvoiceService {

    @Autowired
    InvoiceRepository repo;

    @Autowired
    LeaseRepository leaseRepo;

    @Autowired
    TenantRepository tenantRepo;

    public InvoiceDTO save(InvoiceDTO invoiceDTO) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Invoice lastInvoice = repo.findTopByOrderByIdDesc();

        long nextId = (lastInvoice == null) ? 1 : lastInvoice.getId() + 1;

        String invoiceNo = String.format("INV%06d", nextId);

        Lease lease = leaseRepo.findById(invoiceDTO.getLeaseId())
                .orElseThrow(() -> new CustomException("Lease not found"));

        Tenant tenant = tenantRepo.findById(invoiceDTO.getTenantId())
                .orElseThrow(() -> new CustomException("Tenant not found"));

        Invoice invoice = new Invoice();
        invoice.setInvoiceNo(invoiceNo);
        invoice.setLease(lease);
        invoice.setTenant(tenant);
        invoice.setInvoiceType(invoiceDTO.getInvoiceType());
        invoice.setBillingPeriodStart(invoiceDTO.getBillingPeriodStart());
        invoice.setBillingPeriodEnd(invoiceDTO.getBillingPeriodEnd());
        if(invoiceDTO.getIssueDate() != null) {
        	invoice.setIssueDate(invoiceDTO.getIssueDate());
        }else {
        	invoice.setIssueDate(LocalDate.now());
        }
        
        invoice.setDueDate(invoiceDTO.getDueDate());
        invoice.setSubtotal(invoiceDTO.getSubtotal());
        invoice.setDiscount(invoiceDTO.getDiscount());
        invoice.setTaxAmount(invoiceDTO.getTaxAmount());
        invoice.setTotalAmount(invoiceDTO.getTotalAmount());
        invoice.setPaidAmount(invoiceDTO.getPaidAmount());
        
        if (invoiceDTO.getTotalAmount().compareTo(BigDecimal.ZERO) == 0) {
        	invoice.setStatus(InvoiceStatus.DRAFT);
        } else if (invoiceDTO.getPaidAmount().compareTo(BigDecimal.ZERO) == 0) {
            invoice.setStatus(InvoiceStatus.UNPAID);
        } else if (invoiceDTO.getPaidAmount().compareTo(invoiceDTO.getTotalAmount()) < 0) {
            invoice.setStatus(InvoiceStatus.PARTIALLY_PAID);
        } else if (invoiceDTO.getPaidAmount().compareTo(invoiceDTO.getTotalAmount()) == 0) {
            invoice.setStatus(InvoiceStatus.PAID);
        } 

        if (invoiceDTO.getDueDate() != null &&
            invoiceDTO.getDueDate().isBefore(LocalDate.now()) &&
            invoiceDTO.getPaidAmount().compareTo(invoiceDTO.getTotalAmount()) < 0) {
            invoice.setStatus(InvoiceStatus.OVERDUE);
        }   
        
        invoice.setNotes(invoiceDTO.getNotes());
        invoice.setCreatedBy(username);
        if(invoiceDTO.getInvoiceDetails() != null) {
        	for(InvoiceDetailDTO dto: invoiceDTO.getInvoiceDetails()) {
        		InvoiceDetail invDetail = new InvoiceDetail();
        		invDetail.setInvoice(invoice);
        		invDetail.setItemType(dto.getItemType());
        		invDetail.setQuantity(dto.getQuantity());
        		invDetail.setUnitPrice(dto.getUnitPrice());
        		invDetail.setRemarks(dto.getRemarks());
        		invoice.getInvoiceDetails().add(invDetail);
        	}
        }

        invoice = repo.save(invoice);

        return new InvoiceDTO(invoice);
    }

    public InvoiceDTO update(InvoiceDTO invoiceDTO) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Invoice invoice = repo.findById(invoiceDTO.getId())
                .orElseThrow(() -> new CustomException("Invoice not found"));

        Lease lease = leaseRepo.findById(invoiceDTO.getLeaseId())
                .orElseThrow(() -> new CustomException("Lease not found"));

        Tenant tenant = tenantRepo.findById(invoiceDTO.getTenantId())
                .orElseThrow(() -> new CustomException("Tenant not found"));

        invoice.setLease(lease);
        invoice.setTenant(tenant);
        invoice.setInvoiceType(invoiceDTO.getInvoiceType());
        invoice.setBillingPeriodStart(invoiceDTO.getBillingPeriodStart());
        invoice.setBillingPeriodEnd(invoiceDTO.getBillingPeriodEnd());
        if(invoiceDTO.getIssueDate() != null) {
        	invoice.setIssueDate(invoiceDTO.getIssueDate());
        }else {
        	invoice.setIssueDate(LocalDate.now());
        }
        invoice.setDueDate(invoiceDTO.getDueDate());
        invoice.setSubtotal(invoiceDTO.getSubtotal());
        invoice.setDiscount(invoiceDTO.getDiscount());
        invoice.setTaxAmount(invoiceDTO.getTaxAmount());
        invoice.setTotalAmount(invoiceDTO.getTotalAmount());
        invoice.setPaidAmount(invoiceDTO.getPaidAmount());

        if (invoiceDTO.getTotalAmount().compareTo(BigDecimal.ZERO) == 0) {
        	invoice.setStatus(InvoiceStatus.DRAFT);
        } else if (invoiceDTO.getPaidAmount().compareTo(BigDecimal.ZERO) == 0) {
            invoice.setStatus(InvoiceStatus.UNPAID);
        } else if (invoiceDTO.getPaidAmount().compareTo(invoiceDTO.getTotalAmount()) < 0) {
            invoice.setStatus(InvoiceStatus.PARTIALLY_PAID);
        } else if (invoiceDTO.getPaidAmount().compareTo(invoiceDTO.getTotalAmount()) == 0) {
            invoice.setStatus(InvoiceStatus.PAID);
        }

        if (invoiceDTO.getDueDate() != null &&
            invoiceDTO.getDueDate().isBefore(LocalDate.now()) &&
            invoiceDTO.getPaidAmount().compareTo(invoiceDTO.getTotalAmount()) < 0) {
            invoice.setStatus(InvoiceStatus.OVERDUE);
        }   
        
        invoice.setNotes(invoiceDTO.getNotes());
        invoice.setUpdatedBy(username);
        if(invoiceDTO.getInvoiceDetails() != null) {
        	for(InvoiceDetailDTO dto: invoiceDTO.getInvoiceDetails()) {
        		InvoiceDetail invDetail = new InvoiceDetail();
        		invDetail.setInvoice(invoice);
        		invDetail.setItemType(dto.getItemType());
        		invDetail.setQuantity(dto.getQuantity());
        		invDetail.setUnitPrice(dto.getUnitPrice());
        		invDetail.setRemarks(dto.getRemarks());
        		invoice.getInvoiceDetails().add(invDetail);
        	}
        }

        invoice = repo.save(invoice);

        return new InvoiceDTO(invoice);
    }

    public List<InvoiceDTO> getAll(Long leaseId, String leaseCode, String tenantCode, List<InvoiceStatus> statuses) {

        List<InvoiceDTO> dtoList = new ArrayList<>();
        List<Invoice> list = new ArrayList<>();
        if (statuses == null || statuses.isEmpty()) {
        	list = repo.searchByLeaseIdAndLeaseCodeAndTenantCode(leaseId, leaseCode, tenantCode);
        }else {
        	list = repo.searchByLeaseIdAndLeaseCodeAndTenantCodeAndStatuses(leaseId, leaseCode, tenantCode, statuses);
        }        

        for (Invoice invoice : list) {
            dtoList.add(new InvoiceDTO(invoice));
        }

        return dtoList;
    }

    public InvoiceDTO getById(long id) {

        Optional<Invoice> opt = repo.findById(id);        

        if (!opt.isPresent()) {
            throw new CustomException("Invoice not found");
        }
        InvoiceDTO invDTO = new InvoiceDTO(opt.get());
        if(opt.get().getInvoiceDetails() != null) {
        	for(InvoiceDetail invDetail: opt.get().getInvoiceDetails()) {
        		InvoiceDetailDTO dto = new InvoiceDetailDTO(invDetail);
        		invDTO.getInvoiceDetails().add(dto);
        	}
        }

        return invDTO;
    }
}
