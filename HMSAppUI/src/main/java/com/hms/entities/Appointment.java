package com.hms.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;


public class Appointment {

	
	private Doctor doctor;
	private Patient patient;
	private int appointmentId;
	private LocalDate appDate;
	private LocalTime startTime;
	private LocalTime endTime;
    private String reasonForVisit;
    private String appointmentStatus;
    String doctorReport;
    String medicinesSuggested;
    Bill billObj;
    List<Payment> pmtList;
    
	
	public Doctor getDoctor() {
		return doctor;
	}
	public void setDoctor(Doctor doctor) {
		this.doctor = doctor;
	}
	public Patient getPatient() {
		return patient;
	}
	public void setPatient(Patient patient) {
		this.patient = patient;
	}
	public int getAppointmentId() {
		return appointmentId;
	}
	public void setAppointmentId(int appointmentId) {
		this.appointmentId = appointmentId;
	}
	public LocalDate getAppDate() {
		return appDate;
	}
	public void setAppDate(LocalDate appDate) {
		this.appDate = appDate;
	}
	public LocalTime getStartTime() {
		return startTime;
	}
	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}
	public LocalTime getEndTime() {
		return endTime;
	}
	public void setEndTime(LocalTime endTime) {
		this.endTime = endTime;
	}
	public String getReasonForVisit() {
		return reasonForVisit;
	}
	public void setReasonForVisit(String reasonForVisit) {
		this.reasonForVisit = reasonForVisit;
	}
	public String getAppointmentStatus() {
		return appointmentStatus;
	}
	public void setAppointmentStatus(String appointmentStatus) {
		this.appointmentStatus = appointmentStatus;
	}
	public String getDoctorReport() {
		return doctorReport;
	}
	public void setDoctorReport(String doctorReport) {
		this.doctorReport = doctorReport;
	}
	public String getMedicinesSuggested() {
		return medicinesSuggested;
	}
	public void setMedicinesSuggested(String medicinesSuggested) {
		this.medicinesSuggested = medicinesSuggested;
	}
	public Bill getBillObj() {
		return billObj;
	}
	public void setBillObj(Bill billObj) {
		this.billObj = billObj;
	}
	public List<Payment> getPmtList() {
		return pmtList;
	}
	public void setPmtList(List<Payment> pmtList) {
		this.pmtList = pmtList;
	}
	
}
