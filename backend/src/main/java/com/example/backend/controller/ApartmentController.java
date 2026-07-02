package com.example.backend.controller;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.backend.constants.ErrorCodes;
import com.example.backend.dto.ApartmentDTO;
import com.example.backend.enums.ApartmentStatus;
import com.example.backend.exception.CustomException;
import com.example.backend.model.Apartment;
import com.example.backend.model.User;
import com.example.backend.repository.ApartmentRepository;
import com.example.backend.service.ApartmentService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/auth")
public class ApartmentController {

    private static final Logger logger = LoggerFactory.getLogger(ApartmentController.class);

    @Autowired
    ApartmentRepository apartmentRepo;

    @Autowired
    ApartmentService apartmentService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/apartment")
    public ResponseEntity<ApartmentDTO> save(@RequestBody ApartmentDTO apartmentDTO) {
    	
        Optional<Apartment> apt = apartmentRepo.findByApartmentNumber(apartmentDTO.getApartmentNumber());
		if(apt.isPresent())
			throw new CustomException(ErrorCodes.APARTMENT_ALREADY_EXISTS,
					"Apartment Number already exist!");

        ApartmentDTO dto = apartmentService.save(apartmentDTO);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/apartment/{id}")
    public ResponseEntity<ApartmentDTO> update(
            @PathVariable("id") Long id,
            @RequestBody ApartmentDTO apartmentDTO) {

        Optional<Apartment> apOpt = apartmentRepo.findById(id);

        if (!apOpt.isPresent()) {
            throw new CustomException("Apartment not found");
        }

        ApartmentDTO dto = apartmentService.update(apartmentDTO);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/apartment")
    public ResponseEntity<List<ApartmentDTO>> getAll(
    		@RequestParam(required = false) String apartmentNumber,
    		@RequestParam(required = false) String building,
			@RequestParam(required = false) List<ApartmentStatus> statuses) 
    {

        List<ApartmentDTO> list = new ArrayList<>();
        list = apartmentService.getAll(apartmentNumber, building, statuses);

        return new ResponseEntity<>(list, HttpStatus.ACCEPTED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/apartment/{id}")
    public ResponseEntity<ApartmentDTO> getById(@PathVariable("id") Long id) {

        ApartmentDTO dto = new ApartmentDTO();
        dto = apartmentService.getById(id);

        return new ResponseEntity<>(dto, HttpStatus.ACCEPTED);
    }
}
