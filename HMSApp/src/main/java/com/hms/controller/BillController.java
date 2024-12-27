package com.hms.controller;

import com.hms.entities.Appointment;
import com.hms.entities.Bill;
import com.hms.exception.InvalidEntityException;
import com.hms.service.BillService;
import com.hms.service.EmailService;

import ch.qos.logback.core.model.Model;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;

import com.hms.service.AppointmentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/bills")
public class BillController {

    @Autowired
    private BillService billService;
    @GetMapping("/paidbills")
    public List<Bill> getPaidBills(@RequestParam String patientId,
                                   @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                   @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return billService.getPaidBillsByPatient(patientId, startDate, endDate);
    }

    
    @PostMapping("/generateBill/{appointmentId}")
    public ResponseEntity<Object> generateBill(
            @Valid @RequestBody Bill bill,
           
            @PathVariable int appointmentId) throws MessagingException, InvalidEntityException, IOException  {

      
          
            Bill generatedBill = billService.generateAndSendBill(bill, appointmentId);

            if (generatedBill != null) {
                return ResponseEntity.status(HttpStatus.CREATED).body(generatedBill);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Return 404 if generation failed
            }
    }

    @GetMapping("/viewAllBills")
    public ResponseEntity<Object> viewAllBills(
            @RequestParam(required = false) Integer billId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {

        try {
            List<Bill> bills = billService.getFilteredBills(billId, startDate, endDate);
            if (bills.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); // Return 204 if no bills found
            }
            return ResponseEntity.status(HttpStatus.OK).body(bills); // Return 200 with the list of bills
        } catch (InvalidEntityException ex) {
           
            Map<String, String> error = Map.of("message", ex.getMessage());
            return ResponseEntity.badRequest().body(error); // Return 400 with the error message
        } catch (IllegalArgumentException ex) {
           
            Map<String, String> error = Map.of("message", ex.getMessage());
            return ResponseEntity.badRequest().body(error); // Return 400 with the error message
        } catch (Exception ex) {
           
            Map<String, String> error = Map.of("message", "An unexpected error occurred: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }



    @GetMapping("/patient/{patientId}/pending")
    public List<Bill> getPendingBills(@PathVariable String patientId) {
        return billService.getPendingBillsForPatient(patientId);
    }


    // Get a specific bill by ID
    @GetMapping("/viewBill/{billId}")
    public ResponseEntity<Bill> viewBill(@PathVariable int billId) throws InvalidEntityException {
        Bill bill = billService.getBillById(billId);
        if (bill != null) {
            // Return HTTP 200 (OK) with the Bill details
            return ResponseEntity.status(HttpStatus.OK).body(bill);
        } else {
            // Return HTTP 404 (Not Found) if the Bill is not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    @PutMapping("/updateMedicineFees/{billId}/{medicineFees}")
    public ResponseEntity<Object> updateMedicineFees(@PathVariable int billId, @PathVariable double medicineFees) throws InvalidEntityException {
        try {
            Bill updatedBill = billService.updateMedicineFees(billId, medicineFees);
            
            if (updatedBill != null) {
                // Return HTTP 200 (OK) with the updated Bill
                return ResponseEntity.status(HttpStatus.OK).body(updatedBill);
            } else {
                // Return HTTP 404 (Not Found) if the Bill is not found
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (InvalidEntityException ex) {
            // Create an error map to capture the error message
            Map<String, String> error = new HashMap<>();
            error.put("message", ex.getMessage());

            // Return the error message with NOT_FOUND status
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
    }

   
    // Update test charges for a bill
    @PutMapping("/updateTestCharge/{billId}/{testCharge}")
    public ResponseEntity<Object> updateTestCharge(@PathVariable int billId, @PathVariable double testCharge) throws InvalidEntityException {
        Bill updatedBill;
		try {
			updatedBill = billService.updateTestCharge(billId, testCharge);
			if (updatedBill != null) {
	            // Return HTTP 200 (OK) with the updated Bill
	            return ResponseEntity.status(HttpStatus.OK).body(updatedBill);
	        } else {
	          
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	        }
		} catch (InvalidEntityException ex) {
			// TODO Auto-generated catch block
			Map<String, String> error = new HashMap<>();
            error.put("message", ex.getMessage());

            // Return the error message with NOT_FOUND status
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
		}
        
    }

    @GetMapping("/consultationFees/{appointmentId}")
    public ResponseEntity<Object> getConsultationFees(@PathVariable int appointmentId) throws InvalidEntityException {
        try {
            // Call service to fetch consultation fee
            double consultationFees = billService.getConsultationFees(appointmentId);

            // Return consultation fee with HTTP status OK
            return ResponseEntity.ok(consultationFees);
        } catch (InvalidEntityException ex) {
            // Create an error map to capture the message
            Map<String, String> error = new HashMap<>();
            error.put("message", ex.getMessage());

            // Return the error message with NOT_FOUND status
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
    }
    @PutMapping("/updateBill/{billId}")
    public ResponseEntity<Bill> updateBill(@PathVariable("billId") Integer billId, @RequestBody Bill bill) throws InvalidEntityException {
        // Find the existing bill by ID
        Bill existingBill = billService.getBillById(billId);
        
        if (existingBill == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        
        // Update the bill fields (you can modify this part as needed)
        existingBill.setDescription(bill.getDescription());
        existingBill.setConsultationFees(bill.getConsultationFees());
        existingBill.setMedicineFees(bill.getMedicineFees());
        existingBill.setTestCharge(bill.getTestCharge());

        // Save the updated bill
        Bill updatedBill = billService.updateBill(existingBill);
        
        return ResponseEntity.ok(updatedBill);
    }


    }


