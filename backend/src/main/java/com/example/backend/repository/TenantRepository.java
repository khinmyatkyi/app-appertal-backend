package com.example.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.backend.model.Tenant;

public interface TenantRepository
extends JpaRepository<Tenant, Long> {
	
	Tenant findTopByOrderByIdDesc();

	Optional<Tenant> findByFirstName(String firstName);
	
	@Query("SELECT t FROM Tenant t "
			+ "WHERE (:firstName IS NULL OR LOWER(t.firstName) LIKE LOWER(CONCAT('%', :firstName, '%'))) "
			+ "AND (:lastName IS NULL OR LOWER(t.lastName) LIKE LOWER(CONCAT('%', :lastName, '%'))) "
			+ "AND (:phone IS NULL OR LOWER(t.phone) LIKE LOWER(CONCAT('%', :phone, '%'))) ")
	List<Tenant> searchByFirstNameAndLastNameAndPhone(@Param("firstName") String firstName,
			@Param("lastName") String lastName,
			@Param("phone") String phone);
}