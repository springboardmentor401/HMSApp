package com.hms.controller;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
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

	@Autowired
	private DoctorRepository doctorRepository;

	@Autowired
	private PatientRepository patientRepository;

	@GetMapping("/{apid}")
	public ResponseEntity<Appointment> getAppointmentById(@PathVariable int apid) throws InvalidEntityException {
		Appointment appointment = appointmentService.getAppointmentById(apid);

		return new ResponseEntity<>(appointment, HttpStatus.OK);
	}
	@PostMapping("/bookAppointment/{doctorId}/{patientId}")
	public ResponseEntity<?> createAppointment(@PathVariable("doctorId") int doctorId, 
			@PathVariable("patientId") int patientId, 
			@Valid @RequestBody Appointment appointment, 
			BindingResult result) throws InvalidEntityException {
		if (result.hasErrors()) {
			StringBuilder errorMessages = new StringBuilder();
			result.getAllErrors().forEach(error -> errorMessages.append(error.getDefaultMessage()).append("; "));
			return new ResponseEntity<>(errorMessages.toString(), HttpStatus.BAD_REQUEST);
		}
		Doctor doctor = doctorRepository.findById(doctorId).orElseThrow(() -> new InvalidEntityException("Doctor not found"));
		Patient patient = patientRepository.findById(patientId).orElseThrow(() -> new InvalidEntityException("Patient not found"));
		appointment.setDoctorObj(doctor);
		appointment.setPatientObj(patient);
		Appointment savedAppointment = appointmentService.saveAppointment(appointment);
		if (savedAppointment != null) {
			return new ResponseEntity<>(savedAppointment, HttpStatus.CREATED);
		} else {
			throw new InvalidEntityException("Error occurred while saving the appointment.");
		}
	}
	@PutMapping("/reschedule/{appointmentId}")
	public ResponseEntity<String> rescheduleAppointment(
			@PathVariable int appointmentId,
			@RequestParam LocalDate newDate,
			@RequestParam LocalTime newTime) {
		String response = appointmentService.rescheduleAppointment(appointmentId, newDate, newTime);
		return ResponseEntity.ok(response);
	}
	@DeleteMapping("/cancel/{appointmentId}")
	public ResponseEntity<String> cancelAppointment(@PathVariable int appointmentId) {
		String response = appointmentService.cancelAppointment(appointmentId);
		return ResponseEntity.ok(response);
	}
}
