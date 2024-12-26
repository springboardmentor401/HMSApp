package com.hms.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hms.entities.Appointment;
import com.hms.entities.Patient;

public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
	


	List<Appointment> findByDoctorObj_DoctorId(Integer doctorId);
	
    @Query("SELECT a FROM Appointment a WHERE a.doctorObj.doctorId = :doctorId AND a.appointmentDate = :appointmentDate AND (a.startTime <= :endTime AND a.endTime >= :startTime)")
    List<Appointment> findAppointmentsByDoctorAndTimeOverlap(int doctorId, LocalDate appointmentDate, LocalTime startTime, LocalTime endTime);
    

    @Query("SELECT a.patientObj FROM Appointment a WHERE a.doctorObj.doctorId = :doctorId AND a.appointmentDate = :appDate")
    List<Patient> findPatientsByDoctorAndDate(@Param("doctorId") int doctorId, @Param("appDate") LocalDate appDate);
    
    @Query("SELECT a.patientObj FROM Appointment a WHERE a.status = 'pending'")
    List<Patient> findPatientsWithpendingAppointments();
	List<Appointment> findByStatus(String string);

}

