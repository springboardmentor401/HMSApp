package com.hms.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hms.entities.Appointment;
import com.hms.entities.Bill;

public interface BillRepository extends JpaRepository<Bill, Integer> {
	 Optional<Bill> findByAppointment(Appointment appointment);
	 @Query("SELECT b, SUM(IFNULL(p.amountPaid, 0)) AS totalPaid " +
		       "FROM Bill b LEFT JOIN b.pmtList p " +
		       "WHERE b.appointment.patientObj.patientId = :patientId " +  // Use patientObj here
		       "GROUP BY b.billId " +
		       "HAVING SUM(IFNULL(p.amountPaid, 0)) < b.totalAmount")
		List<Object[]> findBillsWithPendingPaymentsByPatientId(@Param("patientId") String patientId);
		
		
	    List<Bill> findByBillDateBetween(LocalDate startDate, LocalDate endDate);  
	    
	    @Query("SELECT DISTINCT b FROM Bill b " +
	    	       "JOIN b.pmtList p " +
	    	       "JOIN b.appointment a " +
	    	       "WHERE a.patientObj.id = :patientId " +
	    	       "AND b.totalAmount <= (SELECT SUM(p.amountPaid) FROM Payment p WHERE p.billObj.billId = b.billId) " +
	    	       "AND (b.billDate BETWEEN :startDate AND :endDate OR :startDate IS NULL OR :endDate IS NULL) " +
	    	       "ORDER BY b.billDate DESC")
	    	List<Bill> findPaidBillsByPatientAndDateRange(@Param("patientId") String patientId, 
	    	                                              @Param("startDate") LocalDate startDate, 
	    	                                              @Param("endDate") LocalDate endDate);
}