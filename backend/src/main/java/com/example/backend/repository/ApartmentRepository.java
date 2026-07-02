package com.example.backend.repository;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.backend.enums.ApartmentStatus;
import com.example.backend.model.Apartment;

public interface ApartmentRepository
extends JpaRepository<Apartment, Long> {
	
	Apartment findTopByOrderByIdDesc();
	
	Optional<Apartment> findByApartmentNumber(String apartmentNumber);
	
	@Query("SELECT ap FROM Apartment ap "
		    + "WHERE (:apartmentNumber IS NULL OR LOWER(ap.apartmentNumber) LIKE LOWER(CONCAT('%', :apartmentNumber, '%'))) "
		    + "AND (:building IS NULL OR LOWER(ap.building) LIKE LOWER(CONCAT('%', :building, '%'))) ")
	List<Apartment> searchByApartmentNumberAndBuilding(
			@Param("apartmentNumber") String apartmentNumber,
			@Param("building") String building);
	
	@Query("SELECT ap FROM Apartment ap "
		    + "WHERE (:apartmentNumber IS NULL OR LOWER(ap.apartmentNumber) LIKE LOWER(CONCAT('%', :apartmentNumber, '%'))) "
		    + "AND (:building IS NULL OR LOWER(ap.building) LIKE LOWER(CONCAT('%', :building, '%'))) "
		    + "AND (ap.status IN :statuses) ")
	List<Apartment> searchByApartmentNumberAndBuildingAndStatuses(
			@Param("apartmentNumber") String apartmentNumber,
			@Param("building") String building,
			@Param("statuses") List<ApartmentStatus> statuses);

}