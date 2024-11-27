package com.hms.entities;

import java.time.LocalDate;

public class Doctor {

	int doctorId;
	public int getDoctorId() {
		return doctorId;
	}
	public void setDoctorId(int doctorId) {
		this.doctorId = doctorId;
	}
	public String getDoctorName() {
		return doctorName;
	}
	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}
	public String getSpecialization() {
		return specialization;
	}
	public void setSpecialization(String specialization) {
		this.specialization = specialization;
	}
	public String getQualification() {
		return qualification;
	}
	public void setQualification(String qualification) {
		this.qualification = qualification;
	}
	public String getContactNumber() {
		return contactNumber;
	}
	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getLocation() {
		return Location;
	}
	public void setLocation(String location) {
		Location = location;
	}
	public LocalDate getDateofJoining() {
		return dateofJoining;
	}
	public void setDateofJoining(LocalDate dateofJoining) {
		this.dateofJoining = dateofJoining;
	}
	public boolean isSurgeon() {
		return isSurgeon;
	}
	public void setSurgeon(boolean isSurgeon) {
		this.isSurgeon = isSurgeon;
	}
	public int getYearsOfExperience() {
		return yearsOfExperience;
	}
	public void setYearsOfExperience(int yearsOfExperience) {
		this.yearsOfExperience = yearsOfExperience;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	String doctorName;
	String specialization;
	String qualification;
	String contactNumber;
	String emailId;
	String Location;
	LocalDate dateofJoining;
	boolean isSurgeon;
	int yearsOfExperience ;
	boolean status;
	
}
