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
    @Pattern(regexp = "^[6-9][0-9]{9}$", message = "Contact number must start with 6, 7, 8, or 9 and be a 10-digit number")
    private String contactNumber;


    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    @NotBlank(message = "Gender is required")
    @Pattern(regexp = "^(Male|Female|Other)$", message = "Gender must be 'Male', 'Female', or 'Other'")
    private String gender;
    
    @NotBlank(message = "Allergy information is required")
    @Pattern(regexp = "^[A-Za-z,\\s-]+$", message = "Allergy information must contain only alphabetic characters, commas, spaces, and hyphens")
    @Size(min = 4, max = 200, message = "Allergy information must be between 4 and 200 characters")
    private String allergies;

    @NotBlank(message = "Medication information is required")
    @Pattern(regexp = "^[A-Za-z,\\s.-]+$", message = "Medication information must contain only alphabetic characters, commas, spaces, periods, and hyphens")
    @Size(min = 4, max = 50, message = "Medication information must be between 4 and 50 characters")
    private String medications;


    @NotBlank(message = "Email ID is required")
    @Email(message = "Email ID must be valid")
    @Size(min = 3, max = 50, message = "Email ID must be between 3 and 50 characters")
    private String emailId;

    @NotBlank(message = "Location is required")
    @Pattern(regexp = "^[A-Za-z0-9,\\s.-]+$", message = "Location must contain only alphabetic characters, numbers, commas, spaces, periods, and hyphens")
    @Size(min = 3, max = 50, message = "Location must be between 3 and 50 characters")
    private String location;


    @NotBlank(message = "Treatment is required")
    @Pattern(regexp = "^[A-Za-z0-9,\\s.-]+$", message = "Treatment must contain only alphabetic characters, numbers, commas, spaces, periods, and hyphens")
    @Size(min = 3, max = 50, message = "Treatment must be between 3 and 50 characters")
    private String treatments;


    @NotBlank(message = "Medical history is required")
    @Pattern(regexp = "^[A-Za-z0-9,\\s.-]+$", message = "Medical history must contain only alphabetic characters, numbers, commas, spaces, periods, and hyphens")
    @Size(min = 3, max = 50, message = "Medical history must be between 3 and 50 characters")
    private String medicalHistory;


    @NotBlank(message = "Other information is required")
    @Pattern(regexp = "^[A-Za-z0-9,\\s.-]+$", message = "Other information must contain only alphabetic characters, numbers, commas, spaces, periods, and hyphens")
    @Size(min = 3, max = 50, message = "Other information must be between 3 and 50 characters")
    private String others;

    @NotNull(message = "Status is required")
    private Boolean status;  // Wrapper class allows null value, use @NotNull if needed


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
