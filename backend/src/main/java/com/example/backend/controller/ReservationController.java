package com.example.backend.controller;

import java.time.LocalDate;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.backend.dto.ReservationDTO;
import com.example.backend.enums.ApartmentStatus;
import com.example.backend.enums.ReservationStatus;
import com.example.backend.exception.CustomException;
import com.example.backend.model.Reservation;
import com.example.backend.repository.ReservationRepository;
import com.example.backend.service.ReservationService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/auth")
public class ReservationController {

    private static final Logger logger = LoggerFactory.getLogger(ReservationController.class);

    @Autowired
    ReservationRepository reservationRepo;

    @Autowired
    ReservationService reservationService;

    @PostMapping("/reservation")
    public ResponseEntity<ReservationDTO> save(@RequestBody ReservationDTO reservationDTO) {

        ReservationDTO dto = reservationService.save(reservationDTO);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/reservation/{id}")
    public ResponseEntity<ReservationDTO> update(
            @PathVariable("id") Long id,
            @RequestBody ReservationDTO reservationDTO) {

        Optional<Reservation> resOpt = reservationRepo.findById(id);

        if (!resOpt.isPresent()) {
            throw new CustomException("Reservation not found");
        }

        ReservationDTO dto = reservationService.update(reservationDTO);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/reservation")
    public ResponseEntity<List<ReservationDTO>> getAll(
    		@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate reservationDate,
    		@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate moveInDate,
			@RequestParam(required = false) ReservationStatus status) 
    {

        List<ReservationDTO> list = new ArrayList<>();
        list = reservationService.getAll(reservationDate, moveInDate, status);

        return new ResponseEntity<>(list, HttpStatus.ACCEPTED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/reservation/{id}")
    public ResponseEntity<ReservationDTO> getById(@PathVariable("id") Long id) {

        ReservationDTO dto = new ReservationDTO();
        dto = reservationService.getById(id);

        return new ResponseEntity<>(dto, HttpStatus.ACCEPTED);
    }
}