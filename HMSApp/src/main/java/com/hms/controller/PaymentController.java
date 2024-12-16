package com.hms.controller;

import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import com.hms.entities.Bill;
import com.hms.entities.Payment;
import com.hms.exception.InvalidEntityException;
import com.hms.repository.BillRepository;
import com.hms.service.BillService;
import com.hms.service.PaymentService;
import com.hms.exception.*;
import jakarta.validation.Valid;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService service;
    
    @Autowired
    private BillService billService;
    
    @Autowired
    private BillRepository billRepository;

    // Add a payment and send email notification

    @PostMapping("/submit-payment")
    public ResponseEntity<Void> processPayment(@RequestParam int billId,
                                               @RequestParam Double paymentAmount,
                                               @RequestParam String paymentMethod,
                                               @RequestParam String transactionId,
                                               @RequestParam String paymentStatus) {
        try {
            System.out.println("Received billId: " + billId);
            System.out.println("Received paymentAmount: " + paymentAmount);
            System.out.println("Received paymentMethod: " + paymentMethod);
            System.out.println("Received transactionId: " + transactionId);
            System.out.println("Received paymentStatus: " + paymentStatus);

            Bill bill = billService.getBillById(billId);
            if (bill == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            // Create and save the payment
            Payment payment = new Payment();
            payment.setAmountPaid(paymentAmount);
            payment.setPaymentMethod(paymentMethod);
            payment.setPaymentDate(LocalDate.now());
            payment.setPaymentStatus(paymentStatus);
            payment.setTransactionId(transactionId);
            service.addPayment(payment, billId);

            // Update the bill's total paid amount
            double updatedTotalPaid = bill.getTotalPaid() + paymentAmount;
            bill.setTotalPaid(updatedTotalPaid);
            billRepository.save(bill);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }




    @PostMapping("/add/{billId}")
    public ResponseEntity<?> addPayment(@Valid @RequestBody Payment payment, @PathVariable int billId)throws InvalidEntityException {
   
    	  
        if (payment.getPaymentId() != null && !service.isPaymentIdUnique(payment.getPaymentId())) {
            return new ResponseEntity<>("Payment ID already exists", HttpStatus.BAD_REQUEST);
        }

        // Add the payment if validation passes
      
            Payment savedPayment = service.addPayment(payment, billId);

            // Send email notification
          service.sendPaymentConfirmationEmail(savedPayment);

            // Return success response
            return ResponseEntity.ok("Payment added and email sent successfully!");  
        
    }




    @GetMapping("/viewAllPayments")
    public ResponseEntity<List<Payment>> viewAllPayments(
            @RequestParam(required = false) String paymentStatus,
            @RequestParam(required = false) LocalDate paymentDateFrom,
            @RequestParam(required = false) LocalDate paymentDateTo,
            @RequestParam(required = false, defaultValue = "0") Double minAmount,
            @RequestParam(required = false) Double maxAmount,
            @RequestParam(required = false) String paymentMethod) {  // Added paymentMethod filter
        try {
            // Filtering logic in the service
            List<Payment> payments = service.filterPayments(paymentStatus, paymentDateFrom, paymentDateTo, minAmount, maxAmount, paymentMethod);
            return ResponseEntity.ok(payments);
        } catch (Exception e) {
            // Log the exception for debugging purposes
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ArrayList<>());
        }
    }




    // View a specific payment by ID
    @GetMapping("/getById/{id}")
    public ResponseEntity<Payment> viewPaymentById(@PathVariable Long id) {
        try {
            Payment payment = service.getPaymentById(id);
            return ResponseEntity.ok(payment);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // Update an existing payment
    @PutMapping("/updatePayment/{id}")
    public ResponseEntity<?> updatePayment(
            @PathVariable Long id, 
            @Valid @RequestBody Payment updatedPayment, 
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errorMessages = bindingResult.getFieldErrors().stream()
                    .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errorMessages);
        }

        try {
            Payment payment = service.updatePayment(id, updatedPayment);
            return ResponseEntity.ok(payment);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Payment not found");
        }
    }

    // Delete a payment by ID
    @DeleteMapping("/deletePayment/{id}")
    public ResponseEntity<String> deletePayment(@PathVariable Long id) {
        try {
            service.deletePayment(id);
            return ResponseEntity.ok("Payment deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Payment not found");
        }
    }
}
