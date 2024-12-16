package com.hms.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hms.entities.Appointment;
import com.hms.entities.Patient;

public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
    @Query("SELECT a.patientObj FROM Appointment a WHERE a.doctorObj.doctorId = :doctorId AND a.appDate = :appDate")
    List<Patient> findPatientsByDoctorAndDate(@Param("doctorId") int doctorId, @Param("appDate") LocalDate appDate);
}
