package com.example.backend.controller;

import java.time.LocalDate;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.backend.dto.PaymentDTO;
import com.example.backend.enums.ApartmentStatus;
import com.example.backend.enums.PaymentMethod;
import com.example.backend.exception.CustomException;
import com.example.backend.model.Payment;
import com.example.backend.repository.PaymentRepository;
import com.example.backend.service.PaymentService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/auth")
public class PaymentController {

    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    @Autowired
    PaymentRepository paymentRepo;

    @Autowired
    PaymentService paymentService;

    @PreAuthorize("hasRole('ADMIN') OR hasRole('GUEST_ADMIN')")
    @PostMapping("/payment")
    public ResponseEntity<PaymentDTO> save(@RequestBody PaymentDTO paymentDTO) {

        PaymentDTO dto = paymentService.save(paymentDTO);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN') OR hasRole('GUEST_ADMIN')")
    @PutMapping("/payment/{id}")
    public ResponseEntity<PaymentDTO> update(
            @PathVariable("id") Long id,
            @RequestBody PaymentDTO paymentDTO) {

        Optional<Payment> paymentOpt = paymentRepo.findById(id);

        if (!paymentOpt.isPresent()) {
            throw new CustomException("Payment not found");
        }

        PaymentDTO dto = paymentService.update(paymentDTO);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN') OR hasRole('GUEST_ADMIN')")
    @GetMapping("/payment")
    public ResponseEntity<List<PaymentDTO>> getAll(
    		@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate paymentDate,
    		@RequestParam(required = false) PaymentMethod paymentMethod,
			@RequestParam(required = false) String leaseCode) 
    {

        List<PaymentDTO> list = new ArrayList<>();
        list = paymentService.getAll(paymentDate, paymentMethod, leaseCode);

        return new ResponseEntity<>(list, HttpStatus.ACCEPTED);
    }

    @PreAuthorize("hasRole('ADMIN') OR hasRole('GUEST_ADMIN')")
    @GetMapping("/payment/{id}")
    public ResponseEntity<PaymentDTO> getById(@PathVariable("id") Long id) {

        PaymentDTO dto = new PaymentDTO();
        dto = paymentService.getById(id);

        return new ResponseEntity<>(dto, HttpStatus.ACCEPTED);
    }
}