package com.hms.repository;

import com.hms.entities.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Integer> {
    @Query("SELECT p FROM Patient p WHERE p.medicalHistory LIKE %:medicalHistory%")
	List<Patient> findByMedicalHistory(@Param("medicalHistory") String medicalHistory);
    List<Patient> findByMedicalHistoryContaining(String medicalHistory);

    // Query to fetch treatments
    
    public List<Patient> findByPatientName(String patientName);

}
