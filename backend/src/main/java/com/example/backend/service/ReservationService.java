package com.example.backend.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.backend.dto.ReservationDTO;
import com.example.backend.enums.ApartmentStatus;
import com.example.backend.enums.ReservationStatus;
import com.example.backend.exception.CustomException;
import com.example.backend.model.Apartment;
import com.example.backend.model.Reservation;
import com.example.backend.model.Tenant;
import com.example.backend.repository.ApartmentRepository;
import com.example.backend.repository.ReservationRepository;
import com.example.backend.repository.TenantRepository;

@Service
public class ReservationService {

    @Autowired
    ReservationRepository repo;

    @Autowired
    ApartmentRepository apartmentRepo;

    @Autowired
    TenantRepository tenantRepo;

    public ReservationDTO save(ReservationDTO dto) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Apartment apartment = null;
        if (dto.getApartmentId() != null) {
            apartment = apartmentRepo.findById(dto.getApartmentId())
                    .orElseThrow(() -> new CustomException("Apartment not found"));
        }

        Tenant tenant = tenantRepo.findById(dto.getTenantId())
                .orElseThrow(() -> new CustomException("Tenant not found"));

        Reservation reservation = new Reservation();
        reservation.setReservationDate(dto.getReservationDate());
        reservation.setMoveInDate(dto.getMoveInDate());
        reservation.setMoveOutDate(dto.getMoveOutDate());
        reservation.setStatus(dto.getStatus());
        reservation.setApartment(apartment);
        reservation.setTenant(tenant);
        reservation.setNotes(dto.getNotes());
        reservation.setCreatedBy(username);
        
        if(dto.getStatus() == ReservationStatus.CONFIRMED) {
        	apartment.setStatus(ApartmentStatus.RESERVED);
        	apartmentRepo.save(apartment);
        }

        reservation = repo.save(reservation);

        return new ReservationDTO(reservation);
    }

    public ReservationDTO update(ReservationDTO dto) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Reservation reservation = repo.findById(dto.getId())
                .orElseThrow(() -> new CustomException("Reservation not found"));

        Apartment apartment = null;
        if (dto.getApartmentId() != null) {
            apartment = apartmentRepo.findById(dto.getApartmentId())
                    .orElseThrow(() -> new CustomException("Apartment not found"));
        }

        Tenant tenant = tenantRepo.findById(dto.getTenantId())
                .orElseThrow(() -> new CustomException("Tenant not found"));

        reservation.setReservationDate(dto.getReservationDate());
        reservation.setMoveInDate(dto.getMoveInDate());
        reservation.setMoveOutDate(dto.getMoveOutDate());
        reservation.setStatus(dto.getStatus());
        reservation.setApartment(apartment);
        reservation.setTenant(tenant);
        reservation.setNotes(dto.getNotes());
        reservation.setUpdatedBy(username);
        
        if(dto.getStatus() == ReservationStatus.CONFIRMED) {
        	apartment.setStatus(ApartmentStatus.RESERVED);
        	apartmentRepo.save(apartment);
        }

        reservation = repo.save(reservation);

        return new ReservationDTO(reservation);
    }

    public List<ReservationDTO> getAll(LocalDate reservationDate, LocalDate moveInDate, ReservationStatus status) {

        List<ReservationDTO> list = new ArrayList<>();
        List<Reservation> entities = repo.searchByReservationDateAndMoveInDateAndStatus(reservationDate, moveInDate, status);

        for (Reservation r : entities) {
            list.add(new ReservationDTO(r));
        }

        return list;
    }

    public ReservationDTO getById(long id) {

        Optional<Reservation> opt = repo.findById(id);

        if (!opt.isPresent()) {
            throw new CustomException("Reservation not found");
        }

        return new ReservationDTO(opt.get());
    }
}
