package com.example.backend.controller;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.backend.dto.InvoiceDTO;
import com.example.backend.enums.ApartmentStatus;
import com.example.backend.enums.InvoiceStatus;
import com.example.backend.exception.CustomException;
import com.example.backend.model.Invoice;
import com.example.backend.repository.InvoiceRepository;
import com.example.backend.service.InvoiceService;

@RestController
@RequestMapping("/api/auth")
public class InvoiceController {

    private static final Logger logger = LoggerFactory.getLogger(InvoiceController.class);

    @Autowired
    InvoiceRepository invoiceRepo;

    @Autowired
    InvoiceService invoiceService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/invoice")
    public ResponseEntity<InvoiceDTO> save(@RequestBody InvoiceDTO invoiceDTO) {

        InvoiceDTO dto = invoiceService.save(invoiceDTO);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/invoice/{id}")
    public ResponseEntity<InvoiceDTO> update(
            @PathVariable("id") Long id,
            @RequestBody InvoiceDTO invoiceDTO) {

        Optional<Invoice> invOpt = invoiceRepo.findById(id);

        if (!invOpt.isPresent()) {
            throw new CustomException("Invoice not found");
        }

        invoiceDTO.setId(id);

        InvoiceDTO dto = invoiceService.update(invoiceDTO);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/invoice")
    public ResponseEntity<List<InvoiceDTO>> getAll(
    		@RequestParam(required = false) Long leaseId,
    		@RequestParam(required = false) String leaseCode,
    		@RequestParam(required = false) String tenantCode,
    		@RequestParam(required = false) List<InvoiceStatus> statuses) {

        List<InvoiceDTO> list = invoiceService.getAll(leaseId, leaseCode, tenantCode, statuses);

        return new ResponseEntity<>(list, HttpStatus.ACCEPTED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/invoice/{id}")
    public ResponseEntity<InvoiceDTO> getById(@PathVariable("id") Long id) {

        InvoiceDTO dto = invoiceService.getById(id);

        return new ResponseEntity<>(dto, HttpStatus.ACCEPTED);
    }
}