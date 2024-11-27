package com.hms.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hms.entities.Appointment;
import com.hms.entities.Bill;
import com.hms.repository.AppointmentRepository;
import com.hms.repository.BillRepository;

@Service
public class BillService {

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private AppointmentRepository appRepository;

    // Generate a bill
    public Bill generateBill(Bill bill, Integer appointmentId) {
        Optional<Appointment> op = appRepository.findById(appointmentId);
        if (op.isPresent()) {
            Appointment appointment = op.get();
            bill.setAppointment(appointment);  // Set the appointment for the bill
            bill.setBillDate(LocalDate.now());  // Set the current date for the bill
            return billRepository.save(bill);  // Save the bill to the database
        } else {
            return null;  // Return null if appointment not found
        }
    }

    // Get all bills
    public List<Bill> getAllBills() {
        return billRepository.findAll();
    }

    // Get a specific bill by ID
    public Bill getBillById(Long billId) {
        return billRepository.findById(billId).orElse(null); // Return the bill or null if not found
    }

    // Update medicine fees for a bill
    public Bill updateMedicineFees(Long billId, double medicineFees) {
        Optional<Bill> billOptional = billRepository.findById(billId);
        if (billOptional.isPresent()) {
            Bill bill = billOptional.get();
            bill.setMedicineFees(medicineFees);  // Update the medicine fees
            return billRepository.save(bill);    // Save and return the updated bill
        } else {
            return null;  // Return null if bill not found
        }
    }

    // Update test charges for a bill
    public Bill updateTestCharge(Long billId, double testCharge) {
        Optional<Bill> billOptional = billRepository.findById(billId);
        if (billOptional.isPresent()) {
            Bill bill = billOptional.get();
            bill.setTestCharge(testCharge);       // Update the test charge
            return billRepository.save(bill);    // Save and return the updated bill
        } else {
            return null;  // Return null if bill not found
        }
    }
}
