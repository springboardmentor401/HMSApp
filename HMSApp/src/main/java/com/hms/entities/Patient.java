package com.hms.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Entity
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int patientId;

    @NotBlank(message = "Patient name is required")
    @Size(min = 3, max = 50, message = "Patient name must be between 3 and 50 characters")
    private String patientName;

    @NotBlank(message = "Contact number is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Contact number must be a 10-digit number")
    private String contactNumber;

    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    @NotBlank(message = "Gender is required")
    @Pattern(regexp = "^(Male|Female|Other)$", message = "Gender must be 'Male', 'Female', or 'Other'")
    private String gender;

    @Size(min = 3, max = 50, message = "Allergies description must be between 3 and 50 characters")
    private String allergies;

    @Size(min = 3, max = 50, message = "Medications description must be between 3 and 50 characters")
    private String medications;

    @NotBlank(message = "Email ID is required")
    @Email(message = "Email ID must be valid")
    @Size(min = 3, max = 50, message = "Email ID must be between 3 and 50 characters")
    private String emailId;

    @Size(min = 3, max = 50, message = "Location description must be between 3 and 50 characters")
    private String location;

    @Size(min = 3, max = 50, message = "Treatments description must be between 3 and 50 characters")
    private String treatments;

    @Size(min = 3, max = 50, message = "Medical history must be between 3 and 50 characters")
    private String medicalHistory;

    @Size(min = 3, max = 50, message = "Other information must be between 3 and 50 characters")
    private String others;

    private boolean status;

    // Constructors
    public Patient() {}

    // Getters and Setters
    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAllergies() {
        return allergies;
    }

    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }

    public String getMedications() {
        return medications;
    }

    public void setMedications(String medications) {
        this.medications = medications;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTreatments() {
        return treatments;
    }

    public void setTreatments(String treatments) {
        this.treatments = treatments;
    }

    public String getMedicalHistory() {
        return medicalHistory;
    }

    public void setMedicalHistory(String medicalHistory) {
        this.medicalHistory = medicalHistory;
    }

    public String getOthers() {
        return others;
    }

    public void setOthers(String others) {
        this.others = others;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
