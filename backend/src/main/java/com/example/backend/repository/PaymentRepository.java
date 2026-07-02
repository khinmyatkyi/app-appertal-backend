package com.example.backend.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.backend.enums.PaymentMethod;
import com.example.backend.model.Payment;

public interface PaymentRepository
extends JpaRepository<Payment, Long> {
	
	Payment findTopByOrderByIdDesc();
	
	@Query("SELECT pay FROM Payment pay "
	        + "WHERE (:paymentDate IS NULL OR pay.paymentDate >= :paymentDate) "
	        + "AND (:paymentMethod IS NULL OR pay.paymentMethod = :paymentMethod) "
	        + "AND (:leaseCode IS NULL OR LOWER(pay.lease.leaseCode) LIKE LOWER(CONCAT('%', :leaseCode, '%')))")
	List<Payment> searchByPaymentDateAndPaymentMethodAndLeaseId(
	        @Param("paymentDate") LocalDate paymentDate,
	        @Param("paymentMethod") PaymentMethod paymentMethod,
	        @Param("leaseCode") String leaseCode);

}