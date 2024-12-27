package com.hms.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hms.entities.Appointment;
import com.hms.entities.Doctor;
import com.hms.entities.Patient;
import com.hms.exception.InvalidEntityException;
import com.hms.repository.DoctorRepository;
import com.hms.repository.PatientRepository;
import com.hms.service.AppointmentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {
	
	

    @Autowired
    private AppointmentService appointmentService;
    
    
    @GetMapping("/{apid}")
    public ResponseEntity<Appointment> getAppointmentById(@PathVariable int apid) throws InvalidEntityException {
        Appointment appointment = appointmentService.getAppointmentById(apid);
        if(appointment==null) {
        	return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return new ResponseEntity<>(appointment, HttpStatus.OK);
    }
    @PostMapping("/bookAppointment/{doctorId}/{patientId}")
    public ResponseEntity<?> createAppointment(@PathVariable("doctorId") int doctorId, 
                                               @PathVariable("patientId") int patientId, 
                                               @Valid @RequestBody Appointment appointment) throws InvalidEntityException {
        
        Appointment savedAppointment = appointmentService.saveAppointment(appointment, doctorId, patientId);
            return new ResponseEntity<>(savedAppointment, HttpStatus.CREATED);
    }
    @PutMapping("/reschedule/{appointmentId}")
    public ResponseEntity<String> rescheduleAppointment(
            @PathVariable int appointmentId,
            @RequestParam LocalDate newDate,
            @RequestParam LocalTime newTime) throws InvalidEntityException {
        String response = appointmentService.rescheduleAppointment(appointmentId, newDate, newTime);
        return ResponseEntity.ok(response);
    }
    @PutMapping("/cancel/{appointmentId}")
    public ResponseEntity<String> cancelAppointment(@PathVariable("appointmentId") int appointmentId)throws InvalidEntityException {
        try {
            appointmentService.cancelAppointment(appointmentId);
            return ResponseEntity.ok("Appointment has been successfully cancelled.");   
        }
        catch (InvalidEntityException e) {
            return ResponseEntity.status(404).body("Appointment not found.");
        }
        catch (Exception e) {
            return ResponseEntity.status(500).body("An unexpected error occurred while processing your request.");
        }
    }
    @GetMapping("/patientsWithAppointmentCurrentDay")
    public ResponseEntity<List<Patient>> getPatientsWithAppointmentCurrentDay() {
        LocalDate currentDate = LocalDate.now();
        List<Patient> patients = appointmentService.getPatientsWithAppointmentsOnDate(currentDate);
        
        return ResponseEntity.ok(patients);
    }
    @GetMapping("/appointmentsForDate/{date}")
    public List<Appointment> viewAppointmentsForDate(@PathVariable("date") String date) {
        LocalDate requestedDate = LocalDate.parse(date); // Convert string date to LocalDate
        return appointmentService.getAppointmentsForDate(requestedDate);
    }

}
