package com.hms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hms.entities.Payment;
import com.hms.service.PaymentService;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService service;

    // Add a new payment
    @PostMapping("/add/{billId}")
    public ResponseEntity<Payment> addPayment(@RequestBody Payment payment, @PathVariable int billId) {
        Payment createdPayment = service.addPayment(payment, billId);
        if (createdPayment != null) {
            return new ResponseEntity<>(createdPayment, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Bill not found
        }
    }

    // View all payments
    @GetMapping("/viewAllPayments")
    public ResponseEntity<List<Payment>> viewAllPayments() {
        List<Payment> payments = service.getAllPayments();
        return new ResponseEntity<>(payments, HttpStatus.OK);
    }

    // View a specific payment by ID
    @GetMapping("/getById/{id}")
    public ResponseEntity<Payment> viewPaymentById(@PathVariable Integer id) {
        try {
            Payment payment = service.getPaymentById(id);
            return new ResponseEntity<>(payment, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Payment not found
        }
    }

    // Update an existing payment
    @PutMapping("/updatePayment/{id}")
    public ResponseEntity<Payment> updatePayment(@PathVariable Integer id, @RequestBody Payment updatedPayment) {
        try {
            Payment payment = service.updatePayment(id, updatedPayment);
            return new ResponseEntity<>(payment, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Payment not found
        }
    }

    // Delete a payment by ID
    @DeleteMapping("/deletePayment/{id}")
    public ResponseEntity<String> deletePayment(@PathVariable Integer id) {
        try {
            service.deletePayment(id);
            return new ResponseEntity<>("Payment deleted successfully", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Payment not found", HttpStatus.NOT_FOUND);
        }
    }
}
 