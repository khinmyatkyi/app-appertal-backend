package com.example.backend.controller;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.backend.constants.ErrorCodes;
import com.example.backend.dto.TenantDTO;
import com.example.backend.enums.ApartmentStatus;
import com.example.backend.exception.CustomException;
import com.example.backend.model.Tenant;
import com.example.backend.model.User;
import com.example.backend.repository.TenantRepository;
import com.example.backend.service.TenantService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/auth")
public class TenantController {

    private static final Logger logger = LoggerFactory.getLogger(TenantController.class);

    @Autowired
    TenantRepository tenantRepo;

    @Autowired
    TenantService tenantService;

    @PreAuthorize("hasRole('ADMIN') OR hasRole('GUEST_ADMIN')")
    @PostMapping("/tenant")
    public ResponseEntity<TenantDTO> save(@RequestBody TenantDTO tenantDTO) {
    	Optional<Tenant> tenantOpt = tenantRepo.findByFirstName(tenantDTO.getFirstName());
		if(tenantOpt.isPresent())
			throw new CustomException(ErrorCodes.TENANT_ALREADY_EXISTS,
					"Tenant already exist!");
        TenantDTO dto = tenantService.save(tenantDTO);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN') OR hasRole('GUEST_ADMIN')")
    @PutMapping("/tenant/{id}")
    public ResponseEntity<TenantDTO> update(
            @PathVariable("id") Long id,
            @RequestBody TenantDTO tenantDTO) {

        Optional<Tenant> tenantOpt = tenantRepo.findById(id);

        if (!tenantOpt.isPresent()) {
            throw new CustomException("Tenant not found");
        }

        TenantDTO dto = tenantService.update(tenantDTO);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN') OR hasRole('GUEST_ADMIN')")
    @GetMapping("/tenant")
    public ResponseEntity<List<TenantDTO>> getAll(
    		@RequestParam(required = false) String firstName,
    		@RequestParam(required = false) String lastName,
			@RequestParam(required = false) String phone) 
    {

        List<TenantDTO> list = new ArrayList<>();
        list = tenantService.getAll(firstName, lastName, phone);

        return new ResponseEntity<>(list, HttpStatus.ACCEPTED);
    }

    @PreAuthorize("hasRole('ADMIN') OR hasRole('GUEST_ADMIN')")
    @GetMapping("/tenant/{id}")
    public ResponseEntity<TenantDTO> getById(@PathVariable("id") Long id) {

        TenantDTO dto = new TenantDTO();
        dto = tenantService.getById(id);

        return new ResponseEntity<>(dto, HttpStatus.ACCEPTED);
    }
}