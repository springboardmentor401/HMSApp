package com.hms.controller;

import com.hms.entities.Appointment;
import com.hms.entities.Bill;
import com.hms.exception.InvalidEntityException;
import com.hms.service.BillService;

import jakarta.validation.Valid;

import com.hms.service.AppointmentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/bills")
public class BillController {

    @Autowired
    private BillService billService;

    @Autowired
    private AppointmentService appointmentService;

    
    @PostMapping("/generateBill/{appointmentId}")
    public ResponseEntity<Object> generateBill(@Valid @RequestBody Bill bill, BindingResult result, @PathVariable int appointmentId){

        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError e : result.getFieldErrors()) {
                errors.put(e.getField(), e.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }

        float discountPercentage = 0.0f;

        // Check if medicine fees are greater than 1000 and set discount
        if (bill.getMedicineFees() > 1000) {
            discountPercentage = 10.0f; // 10% discount for medicine fees greater than 1000
        }

        // Set the discount percentage in the Bill object
        bill.setDiscountPercentage(discountPercentage);

       

        Bill generatedBill = billService.generateBill(bill, appointmentId);
        if (generatedBill != null) {
            // Return HTTP 201 (Created) status with the generated Bill as the body
            return ResponseEntity.status(HttpStatus.CREATED).body(generatedBill);
        } else {
            // Return HTTP 404 (Not Found) status if the appointment is not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


    // Get all bills
    @GetMapping("/viewAllBills")
    public ResponseEntity<List<Bill>> viewAllBills() {
        List<Bill> bills = billService.getAllBills();
        if (!bills.isEmpty()) {
            // Return HTTP 200 (OK) with the list of bills
            return ResponseEntity.status(HttpStatus.OK).body(bills);
        } else {
            // Return HTTP 204 (No Content) if no bills are found
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }

    // Get a specific bill by ID
    @GetMapping("/viewBill/{billId}")
    public ResponseEntity<Bill> viewBill(@PathVariable int billId) {
        Bill bill = billService.getBillById(billId);
        if (bill != null) {
            // Return HTTP 200 (OK) with the Bill details
            return ResponseEntity.status(HttpStatus.OK).body(bill);
        } else {
            // Return HTTP 404 (Not Found) if the Bill is not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // Update medicine fees for a bill
    @PutMapping("/updateMedicineFees/{billId}/{medicineFees}")
    public ResponseEntity<Bill> updateMedicineFees(@PathVariable int billId, @PathVariable double medicineFees) {
        Bill updatedBill = billService.updateMedicineFees(billId, medicineFees);
        if (updatedBill != null) {
            // Return HTTP 200 (OK) with the updated Bill
            return ResponseEntity.status(HttpStatus.OK).body(updatedBill);
        } else {
            // Return HTTP 404 (Not Found) if the Bill is not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // Update test charges for a bill
    @PutMapping("/updateTestCharge/{billId}/{testCharge}")
    public ResponseEntity<Bill> updateTestCharge(@PathVariable int billId, @PathVariable double testCharge) {
        Bill updatedBill = billService.updateTestCharge(billId, testCharge);
        if (updatedBill != null) {
            // Return HTTP 200 (OK) with the updated Bill
            return ResponseEntity.status(HttpStatus.OK).body(updatedBill);
        } else {
            // Return HTTP 404 (Not Found) if the Bill is not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    @GetMapping("/consultationFees/{appointmentId}")
    public ResponseEntity<Double> getConsultationFees(@PathVariable int appointmentId) throws InvalidEntityException {
    	Appointment appointment = appointmentService.getAppointmentById(appointmentId);
        double consultationFees = appointment.getDoctorObj().getConsultationFees();
        return ResponseEntity.ok(consultationFees);
        
           
        } 
    }


