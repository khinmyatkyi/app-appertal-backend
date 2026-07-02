package com.example.backend.controller;

import java.time.LocalDate;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.backend.dto.MaintenanceRequestDTO;
import com.example.backend.enums.MaintenanceRequestStatus;
import com.example.backend.exception.CustomException;
import com.example.backend.model.MaintenanceRequest;
import com.example.backend.repository.MaintenanceRequestRepository;
import com.example.backend.service.MaintenanceRequestService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/auth")
public class MaintenanceRequestController {

    private static final Logger logger = LoggerFactory.getLogger(MaintenanceRequestController.class);

    @Autowired
    MaintenanceRequestRepository maintenanceRepo;

    @Autowired
    MaintenanceRequestService maintenanceService;

    @PostMapping("/maintenance-request")
    public ResponseEntity<MaintenanceRequestDTO> save(@RequestBody MaintenanceRequestDTO dto) {

        MaintenanceRequestDTO result = maintenanceService.save(dto);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/maintenance-request/{id}")
    public ResponseEntity<MaintenanceRequestDTO> update(
            @PathVariable("id") Long id,
            @RequestBody MaintenanceRequestDTO dto) {

        Optional<MaintenanceRequest> opt = maintenanceRepo.findById(id);

        if (!opt.isPresent()) {
            throw new CustomException("Maintenance request not found");
        }

        MaintenanceRequestDTO result = maintenanceService.update(dto);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/maintenance-request")
    public ResponseEntity<List<MaintenanceRequestDTO>> getAll(
    		@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate requestDate,
    		@RequestParam(required = false) MaintenanceRequestStatus status,
    		@RequestParam(required = false) String tenantCode) 
    {

        List<MaintenanceRequestDTO> list = new ArrayList<>();
        list = maintenanceService.getAll(requestDate, status, tenantCode);

        return new ResponseEntity<>(list, HttpStatus.ACCEPTED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/maintenance-request/{id}")
    public ResponseEntity<MaintenanceRequestDTO> getById(@PathVariable("id") Long id) {

        MaintenanceRequestDTO dto = new MaintenanceRequestDTO();
        dto = maintenanceService.getById(id);

        return new ResponseEntity<>(dto, HttpStatus.ACCEPTED);
    }
}