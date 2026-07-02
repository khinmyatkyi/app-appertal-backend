package com.example.backend.controller;

import java.time.LocalDate;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.backend.dto.LeaseDTO;
import com.example.backend.exception.CustomException;
import com.example.backend.model.Lease;
import com.example.backend.repository.LeaseRepository;
import com.example.backend.service.LeaseService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/auth")
public class LeaseController {

    private static final Logger logger = LoggerFactory.getLogger(LeaseController.class);

    @Autowired
    LeaseRepository leaseRepo;

    @Autowired
    LeaseService leaseService;

    @PostMapping("/lease")
    public ResponseEntity<LeaseDTO> save(@RequestBody LeaseDTO leaseDTO) {

        LeaseDTO dto = leaseService.save(leaseDTO);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/lease/{id}")
    public ResponseEntity<LeaseDTO> update(
            @PathVariable("id") Long id,
            @RequestBody LeaseDTO leaseDTO) {

        Optional<Lease> leaseOpt = leaseRepo.findById(id);

        if (!leaseOpt.isPresent()) {
            throw new CustomException("Lease not found");
        }

        LeaseDTO dto = leaseService.update(leaseDTO);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/lease")
    public ResponseEntity<List<LeaseDTO>> getAll(
    		@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
			@RequestParam(required = false) String leaseCode) 
    {

        List<LeaseDTO> list = new ArrayList<>();
        list = leaseService.getAll(startDate, endDate, leaseCode);

        return new ResponseEntity<>(list, HttpStatus.ACCEPTED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/lease/{id}")
    public ResponseEntity<LeaseDTO> getById(@PathVariable("id") Long id) {

        LeaseDTO dto = new LeaseDTO();
        dto = leaseService.getById(id);

        return new ResponseEntity<>(dto, HttpStatus.ACCEPTED);
    }
}