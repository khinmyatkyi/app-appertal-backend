package com.example.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.backend.enums.InvoiceStatus;
import com.example.backend.model.Invoice;

public interface InvoiceRepository 
extends JpaRepository<Invoice, Long>{

	Invoice findTopByOrderByIdDesc();
	
	@Query("SELECT inv FROM Invoice inv "
	        + "WHERE (:leaseId Is NULL OR inv.lease.id = :leaseId)"
	        + "AND (:leaseCode IS NULL OR LOWER(inv.lease.leaseCode) LIKE LOWER(CONCAT('%', :leaseCode, '%'))) "
	        + "AND (:tenantCode IS NULL OR LOWER(inv.tenant.tenantCode) LIKE LOWER(CONCAT('%', :tenantCode, '%'))) ")	
	List<Invoice> searchByLeaseIdAndLeaseCodeAndTenantCode(
	        @Param("leaseId") Long leaseId,
	        @Param("leaseCode") String leaseCode,
	        @Param("tenantCode") String tenantCode);
	
	@Query("SELECT inv FROM Invoice inv "
	        + "WHERE (:leaseId Is NULL OR inv.lease.id = :leaseId)"
	        + "AND (:leaseCode IS NULL OR LOWER(inv.lease.leaseCode) LIKE LOWER(CONCAT('%', :leaseCode, '%'))) "
	        + "AND (:tenantCode IS NULL OR LOWER(inv.tenant.tenantCode) LIKE LOWER(CONCAT('%', :tenantCode, '%'))) "
	        + "AND (inv.status IN :statuses)")	
	List<Invoice> searchByLeaseIdAndLeaseCodeAndTenantCodeAndStatuses(
	        @Param("leaseId") Long leaseId,
	        @Param("leaseCode") String leaseCode,
	        @Param("tenantCode") String tenantCode,
	        @Param("statuses") List<InvoiceStatus> statuses);
	
}
