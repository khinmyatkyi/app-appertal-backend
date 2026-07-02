package com.example.backend.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.backend.enums.MaintenanceRequestStatus;
import com.example.backend.model.MaintenanceRequest;

public interface MaintenanceRequestRepository
extends JpaRepository<MaintenanceRequest, Long> {

	MaintenanceRequest findTopByOrderByIdDesc();
	
	@Query("SELECT mt FROM MaintenanceRequest mt "
	        + "WHERE (:requestDate IS NULL OR mt.requestDate >= :requestDate) "
	        + "AND (:status IS NULL OR mt.status = :status) "
	        + "AND (:tenantCode IS NULL OR LOWER(mt.tenant.tenantCode) LIKE LOWER(CONCAT('%', :tenantCode, '%')))")
	List<MaintenanceRequest> searchByRequestDateAndStatusAndTenantId(
	        @Param("requestDate") LocalDate requestDate,
	        @Param("status") MaintenanceRequestStatus status,
	        @Param("tenantCode") String tenantCode);
}
