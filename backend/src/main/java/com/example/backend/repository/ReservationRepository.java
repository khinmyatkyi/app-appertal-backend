package com.example.backend.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.backend.enums.ReservationStatus;
import com.example.backend.model.Reservation;

public interface ReservationRepository
extends JpaRepository<Reservation, Long> {
	
	@Query("SELECT res FROM Reservation res "
	        + "WHERE (:reservationDate IS NULL OR res.reservationDate >= :reservationDate) "
	        + "AND (:moveInDate IS NULL OR res.moveInDate <= :moveInDate) "
	        + "AND (:status IS NULL OR res.status = :status) ")
	List<Reservation> searchByReservationDateAndMoveInDateAndStatus(
	        @Param("reservationDate") LocalDate reservationDate,
	        @Param("moveInDate") LocalDate moveInDate,
	        @Param("status") ReservationStatus status);
}