package com.hms.repository;

import com.hms.entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    // Example of a valid custom query method:

	    @Query("SELECT p FROM Payment p WHERE p.id = :id")
	    Payment findPaymentWithDetails(@Param("id") int id);
	


	public List<Payment> findByPaymentStatusAndAmountPaidBetween(String paymentStatus, Double minAmount, Double maxAmount);

   public List<Payment> findByPaymentStatusAndPaymentDateBetween(String paymentStatus, LocalDate paymentDateFrom, LocalDate paymentDateTo);


    // Filter by payment date range
   public   List<Payment> findByPaymentDateBetween(LocalDate paymentDateFrom, LocalDate paymentDateTo);

    // Filter by amount range
   public  List<Payment> findByAmountPaidBetween(Double minAmount, Double maxAmount);

    // Filter by payment status
  public List<Payment> findByPaymentStatus(String paymentStatus);


    // Filter by amount greater than or equal to a minimum amount
  public List<Payment> findByAmountPaidGreaterThanEqual(Double minAmount);

    // Filter by amount less than or equal to a maximum amount
  public    List<Payment> findByAmountPaidLessThanEqual(Double maxAmount);

    // Return all payments if no filters are applied
  public  List<Payment> findAll();
}