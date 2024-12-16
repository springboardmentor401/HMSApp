package com.hms.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hms.entities.Bill;

public interface BillRepository extends JpaRepository<Bill, Integer> {
}