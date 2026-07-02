package com.example.backend.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.backend.dto.LeaseDTO;
import com.example.backend.enums.ApartmentStatus;
import com.example.backend.enums.InvoiceStatus;
import com.example.backend.enums.InvoiceType;
import com.example.backend.enums.Status;
import com.example.backend.exception.CustomException;
import com.example.backend.model.Apartment;
import com.example.backend.model.Invoice;
import com.example.backend.model.Lease;
import com.example.backend.model.Tenant;
import com.example.backend.repository.ApartmentRepository;
import com.example.backend.repository.InvoiceRepository;
import com.example.backend.repository.LeaseRepository;
import com.example.backend.repository.TenantRepository;

@Service
public class LeaseService {

    @Autowired
    LeaseRepository repo;

    @Autowired
    TenantRepository tenantRepo;

    @Autowired
    ApartmentRepository apartmentRepo;
    
    @Autowired
    InvoiceRepository invoiceRepo;

    public LeaseDTO save(LeaseDTO dto) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Tenant tenant = tenantRepo.findById(dto.getTenantId())
                .orElseThrow(() -> new CustomException("Tenant not found"));

        Apartment apartment = apartmentRepo.findById(dto.getApartmentId())
                .orElseThrow(() -> new CustomException("Apartment not found"));
        
        Lease lastLease = repo.findTopByOrderByIdDesc();

        long nextId = (lastLease == null) ? 1 : lastLease.getId() + 1;

        String leaseCode = String.format("LS%06d", nextId);

        Lease lease = new Lease();
        lease.setLeaseCode(leaseCode);
        lease.setStartDate(dto.getStartDate());
        lease.setEndDate(dto.getEndDate());
        lease.setDepositAmount(dto.getDepositAmount());
        lease.setMonthlyRent(dto.getMonthlyRent());
        lease.setNoMonths(dto.getNoMonths());
        lease.setTenant(tenant);
        lease.setApartment(apartment);
        lease.setStatus(dto.getStatus());
        lease.setCreatedBy(username);

        lease = repo.save(lease);
        Integer noOfMonths = lease.getNoMonths();
        
        if (lease.getStatus() == Status.FINISHED) {
        	//update apartment's status to OCCUPIED
        	apartment.setStatus(ApartmentStatus.OCCUPIED);
        	
        	//for invoice
        	Invoice lastInvoice = invoiceRepo.findTopByOrderByIdDesc();
            long nextInvoiceId = (lastInvoice == null) ? 1 : lastInvoice.getId() + 1;
            String invoiceNo = String.format("INV%06d", nextInvoiceId);

            LocalDate billingStart = lease.getStartDate();
            LocalDate billingEnd = billingStart.plusMonths(1).minusDays(1);
            
            LocalDate issueDate = LocalDate.now();
            LocalDate dueDate = billingStart.plusDays(7);
            
            for (int i = 0; i < noOfMonths; i++) 
            {   
            	Invoice invoice = new Invoice();
    	        invoice.setInvoiceNo(invoiceNo);
    	        invoice.setLease(lease);
    	        invoice.setTenant(tenant);
    	        invoice.setInvoiceType(InvoiceType.MONTHLY_RENT);
    	        invoice.setBillingPeriodStart(billingStart);
    	        invoice.setBillingPeriodEnd(billingEnd);
    	        invoice.setIssueDate(issueDate);
    	        invoice.setDueDate(dueDate);
//    	        invoice.setSubtotal(invoiceDTO.getSubtotal());
//    	        invoice.setDiscount(invoiceDTO.getDiscount());
//    	        invoice.setTaxAmount(invoiceDTO.getTaxAmount());
//    	        invoice.setTotalAmount(invoiceDTO.getTotalAmount());
//    	        invoice.setPaidAmount(invoiceDTO.getPaidAmount());
    	        invoice.setStatus(InvoiceStatus.DRAFT);
    	        invoice.setCreatedBy(username);
    	        invoiceRepo.save(invoice);
    	        
    	        nextInvoiceId++;
    	        invoiceNo = String.format("INV%06d", nextInvoiceId);
    	        
    	        billingStart = billingEnd.plusDays(1);
    	        billingEnd = billingStart.plusMonths(1).minusDays(1);
    	        
    	        dueDate = billingStart.plusDays(7);  	        
    	        
            }

        }

        return new LeaseDTO(lease);
    }

    public LeaseDTO update(LeaseDTO dto) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Lease lease = repo.findById(dto.getId())
                .orElseThrow(() -> new CustomException("Lease not found"));

        Tenant tenant = tenantRepo.findById(dto.getTenantId())
                .orElseThrow(() -> new CustomException("Tenant not found"));

        Apartment apartment = apartmentRepo.findById(dto.getApartmentId())
                .orElseThrow(() -> new CustomException("Apartment not found"));
        
        Status oldStatus = lease.getStatus();
        if(oldStatus == Status.FINISHED) {
        	throw new CustomException("Can't change anything in FINISHED state.");
        }

        lease.setStartDate(dto.getStartDate());
        lease.setEndDate(dto.getEndDate());
        lease.setDepositAmount(dto.getDepositAmount());
        lease.setMonthlyRent(dto.getMonthlyRent());
        lease.setNoMonths(dto.getNoMonths());
        lease.setTenant(tenant);
        lease.setApartment(apartment);
        lease.setStatus(dto.getStatus());
        lease.setUpdatedBy(username);

        lease = repo.save(lease);
        Integer noOfMonths = lease.getNoMonths();
        
        if (lease.getStatus() == Status.FINISHED) {
        	//update apartment's status to OCCUPIED
        	apartment.setStatus(ApartmentStatus.OCCUPIED);
        	
        	//for invoice
        	Invoice lastInvoice = invoiceRepo.findTopByOrderByIdDesc();
            long nextInvoiceId = (lastInvoice == null) ? 1 : lastInvoice.getId() + 1;
            String invoiceNo = String.format("INV%06d", nextInvoiceId);

            LocalDate billingStart = lease.getStartDate();
            LocalDate billingEnd = billingStart.plusMonths(1).minusDays(1);
            
            LocalDate issueDate = LocalDate.now();
            LocalDate dueDate = billingStart.plusDays(7);
            
            for (int i = 0; i < noOfMonths; i++) 
            {   
            	Invoice invoice = new Invoice();
    	        invoice.setInvoiceNo(invoiceNo);
    	        invoice.setLease(lease);
    	        invoice.setTenant(tenant);
    	        invoice.setInvoiceType(InvoiceType.MONTHLY_RENT);
    	        invoice.setBillingPeriodStart(billingStart);
    	        invoice.setBillingPeriodEnd(billingEnd);
    	        invoice.setIssueDate(issueDate);
    	        invoice.setDueDate(dueDate);
//    	        invoice.setSubtotal(invoiceDTO.getSubtotal());
//    	        invoice.setDiscount(invoiceDTO.getDiscount());
//    	        invoice.setTaxAmount(invoiceDTO.getTaxAmount());
//    	        invoice.setTotalAmount(invoiceDTO.getTotalAmount());
//    	        invoice.setPaidAmount(invoiceDTO.getPaidAmount());
    	        invoice.setStatus(InvoiceStatus.DRAFT);
    	        invoice.setCreatedBy(username);
    	        invoiceRepo.save(invoice);
    	        
    	        nextInvoiceId++;
    	        invoiceNo = String.format("INV%06d", nextInvoiceId);
    	        
    	        billingStart = billingEnd.plusDays(1);
    	        billingEnd = billingStart.plusMonths(1).minusDays(1);
    	        
    	        dueDate = billingStart.plusDays(7);  	        
    	        
            }

        }

        return new LeaseDTO(lease);
    }

    public List<LeaseDTO> getAll(LocalDate startDate, LocalDate endDate, String leaseCode) {

        List<LeaseDTO> list = new ArrayList<>();
        List<Lease> entities = repo.searchByStartDateAndEndDateAndTenantId(startDate, endDate, leaseCode);

        for (Lease l : entities) {
            list.add(new LeaseDTO(l));
        }

        return list;
    }

    public LeaseDTO getById(long id) {

        Optional<Lease> opt = repo.findById(id);

        if (!opt.isPresent()) {
            throw new CustomException("Lease not found");
        }

        return new LeaseDTO(opt.get());
    }
}
