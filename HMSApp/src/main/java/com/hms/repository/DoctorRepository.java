package com.hms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hms.entities.Doctor;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
	
	List<Doctor> findBySpecialization(String specialization);


    
}