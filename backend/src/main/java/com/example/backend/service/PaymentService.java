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

import com.example.backend.dto.PaymentDTO;
import com.example.backend.enums.InvoiceStatus;
import com.example.backend.enums.PaymentMethod;
import com.example.backend.enums.Status;
import com.example.backend.exception.CustomException;
import com.example.backend.model.Invoice;
import com.example.backend.model.Lease;
import com.example.backend.model.Payment;
import com.example.backend.repository.InvoiceRepository;
import com.example.backend.repository.LeaseRepository;
import com.example.backend.repository.PaymentRepository;

@Service
public class PaymentService {

    @Autowired
    PaymentRepository repo;

    @Autowired
    LeaseRepository leaseRepo;
    
    @Autowired
    InvoiceRepository invoiceRepo;

    public PaymentDTO save(PaymentDTO dto) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Invoice invoice = new Invoice();

        Lease lease = leaseRepo.findById(dto.getLeaseId())
                .orElseThrow(() -> new CustomException("Lease not found"));            
        
        Payment lastPayment = repo.findTopByOrderByIdDesc();

        long nextId = (lastPayment == null) ? 1 : lastPayment.getId() + 1;

        String paymentCode = String.format("PAY%06d", nextId);

        Payment payment = new Payment();
        payment.setPaymentCode(paymentCode);
        payment.setPaymentDate(dto.getPaymentDate());
        payment.setAmount(dto.getAmount());
        payment.setPaymentMethod(dto.getPaymentMethod());
        payment.setPaymentType(dto.getPaymentType());
        payment.setStatus(dto.getStatus());
        payment.setLease(lease);
        
        if(dto.getInvoiceId() != null) {
        	invoice = invoiceRepo.findById(dto.getInvoiceId())
            		.orElseThrow(() -> new CustomException("Invoice not found"));
        	payment.setInvoice(invoice);
        }  
        
        //Update Invoice if payment success
        if(dto.getStatus() == Status.FINISHED && dto.getInvoiceId() != null) {
        	BigDecimal updatePaidAmt = invoice.getPaidAmount().add(dto.getAmount());
        	invoice.setPaidAmount(updatePaidAmt);
        	if (invoice.getPaidAmount().compareTo(invoice.getTotalAmount()) < 0) {
                invoice.setStatus(InvoiceStatus.PARTIALLY_PAID);
            } else if (invoice.getPaidAmount().compareTo(invoice.getTotalAmount()) == 0) {
                invoice.setStatus(InvoiceStatus.PAID);
            }
        	invoiceRepo.save(invoice);
        }
        
        payment.setCreatedBy(username);

        payment = repo.save(payment);

        return new PaymentDTO(payment);
    }

    public PaymentDTO update(PaymentDTO dto) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Invoice invoice = new Invoice();

        Payment payment = repo.findById(dto.getId())
                .orElseThrow(() -> new CustomException("Payment not found"));

        Lease lease = leaseRepo.findById(dto.getLeaseId())
                .orElseThrow(() -> new CustomException("Lease not found"));

        Status oldStatus = payment.getStatus();
        if(oldStatus == Status.FINISHED) {
        	throw new CustomException("Can't change anything in FINISHED payment.");
        }
        payment.setPaymentDate(dto.getPaymentDate());
        payment.setAmount(dto.getAmount());
        payment.setPaymentMethod(dto.getPaymentMethod());
        payment.setPaymentType(dto.getPaymentType());
        payment.setStatus(dto.getStatus());
        payment.setLease(lease);
        
        if(dto.getInvoiceId() != null) {
        	invoice = invoiceRepo.findById(dto.getInvoiceId())
            		.orElseThrow(() -> new CustomException("Invoice not found"));
        	payment.setInvoice(invoice);
        } 
        //Update Invoice if payment success
        if(dto.getStatus() == Status.FINISHED && dto.getInvoiceId() != null) {
        	BigDecimal updatePaidAmt = invoice.getPaidAmount().add(dto.getAmount());
        	invoice.setPaidAmount(updatePaidAmt);
        	if (invoice.getPaidAmount().compareTo(invoice.getTotalAmount()) < 0) {
                invoice.setStatus(InvoiceStatus.PARTIALLY_PAID);
            } else if (invoice.getPaidAmount().compareTo(invoice.getTotalAmount()) == 0) {
                invoice.setStatus(InvoiceStatus.PAID);
            }
        	invoiceRepo.save(invoice);
        }
        
        payment.setUpdatedBy(username);

        payment = repo.save(payment);

        return new PaymentDTO(payment);
    }

    public List<PaymentDTO> getAll(LocalDate paymentDate, PaymentMethod paymentMethod, String leaseCode) {

        List<PaymentDTO> list = new ArrayList<>();
        List<Payment> entities = repo.searchByPaymentDateAndPaymentMethodAndLeaseId(paymentDate, paymentMethod, leaseCode);

        for (Payment p : entities) {
            list.add(new PaymentDTO(p));
        }

        return list;
    }

    public PaymentDTO getById(long id) {

        Optional<Payment> opt = repo.findById(id);

        if (!opt.isPresent()) {
            throw new CustomException("Payment not found");
        }

        return new PaymentDTO(opt.get());
    }
}
