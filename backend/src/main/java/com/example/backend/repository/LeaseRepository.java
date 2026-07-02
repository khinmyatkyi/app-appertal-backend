package com.example.backend.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.backend.model.Lease;

public interface LeaseRepository
extends JpaRepository<Lease, Long> {
	
	Lease findTopByOrderByIdDesc();
	
	@Query("SELECT ls FROM Lease ls "
	        + "WHERE (:startDate IS NULL OR ls.startDate >= :startDate) "
	        + "AND (:endDate IS NULL OR ls.endDate <= :endDate) "
	        + "AND (:leaseCode IS NULL OR LOWER(ls.leaseCode) LIKE LOWER(CONCAT('%', :leaseCode, '%')))")
	List<Lease> searchByStartDateAndEndDateAndTenantId(
	        @Param("startDate") LocalDate startDate,
	        @Param("endDate") LocalDate endDate,
	        @Param("leaseCode") String leaseCode);

}
