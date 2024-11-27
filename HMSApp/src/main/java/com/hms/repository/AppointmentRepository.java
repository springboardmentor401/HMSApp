package com.hms.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hms.entities.Appointment;

public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
}
