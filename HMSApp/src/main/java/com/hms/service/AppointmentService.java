package com.hms.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hms.entities.Appointment;
import com.hms.entities.Doctor;
import com.hms.entities.Patient;
import com.hms.exception.InvalidEntityException;
import com.hms.repository.AppointmentRepository;
import com.hms.repository.DoctorRepository;
import com.hms.repository.PatientRepository;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    public Appointment saveAppointment(Appointment appointment) throws InvalidEntityException {
        // Fetch doctor and patient from the repository using doctorId and patientId
        Doctor doctor = doctorRepository.findById(appointment.getDoctorObj().getDoctorId())
                .orElseThrow(() -> new InvalidEntityException("Doctor not found for ID: " + appointment.getDoctorObj().getDoctorId()));

        Patient patient = patientRepository.findById(appointment.getPatientObj().getPatientId())
                .orElseThrow(() -> new InvalidEntityException("Patient not found for ID: " + appointment.getPatientObj().getPatientId()));

        // Set the Doctor and Patient objects to the appointment
        appointment.setDoctorObj(doctor);
        appointment.setPatientObj(patient);

        // Set other appointment details
        appointment.setAppointmentDate(appointment.getAppointmentDate());
        appointment.setStartTime(appointment.getStartTime());
        appointment.setReasonForVisit(appointment.getReasonForVisit());

        // Calculate the end time by adding 1 hour to the start time
        appointment.calculateEndTime();

        // Check if there is any conflict with existing appointments
        if (hasAppointmentConflict(appointment)) {
            throw new InvalidEntityException("The doctor already has an appointment scheduled during the requested time.");
        }

        // Set the status to "Scheduled" and save the appointment to the database
        appointment.setStatus("Scheduled");
        return appointmentRepository.save(appointment);
    }


    public boolean hasAppointmentConflict(Appointment appointment) {
        List<Appointment> overlappingAppointments = appointmentRepository.findAppointmentsByDoctorAndTimeOverlap(
                appointment.getDoctorObj().getDoctorId(),  // Use appointment.getDoctor() instead of appointment.getDoctorObj()
                appointment.getAppointmentDate(),
                appointment.getStartTime(),
                appointment.getEndTime()
        );
        return !overlappingAppointments.isEmpty();
    }
   
    public String cancelAppointment(int appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        if (appointment.getAppointmentDate().isAfter(LocalDate.now())) {
            appointment.setStatus("Cancelled");
            appointmentRepository.save(appointment);
            return "Appointment cancelled successfully.";
        } else {
            return "Cannot cancel an appointment in the past.";
        }
    }


    public String rescheduleAppointment(int appointmentId, LocalDate newDate, LocalTime newTime) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        if (!appointment.getStatus().equalsIgnoreCase("Scheduled")) {
            return "Only scheduled appointments can be rescheduled.";
        }

        if (appointment.getAppointmentDate().isEqual(LocalDate.now())) {
            return "Appointments cannot be rescheduled on the same day they were booked.";
        }

        // Update the new date and time
        appointment.setAppointmentDate(newDate); // Set the new date
        appointment.setStartTime(newTime);       // Set the new start time
        appointment.calculateEndTime();         // Ensure the end time is updated
        appointmentRepository.save(appointment);

        return "Appointment rescheduled successfully.";
    }


   
    public Appointment getAppointmentById( int apid) throws InvalidEntityException {
        return appointmentRepository.findById(apid)
                .orElseThrow(() -> new InvalidEntityException("Appointment with id " + apid + " not found"));
    }
   
}