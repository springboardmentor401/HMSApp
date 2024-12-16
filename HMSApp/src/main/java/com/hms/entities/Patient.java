package com.hms.entities;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "patient") // Ensures mapping to the correct table
public class Patient {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int patientId;

    @NotBlank(message = "Patient name is required")
    @Pattern(regexp = "^[A-Za-z ]{3,30}$", message = "Name must contain only alphabetic characters and spaces, with a length between 3 and 30 characters.")
    @Column(name = "patient_name") // Maps to the database column 'patient_name'
    private String patientName;

    @NotBlank(message = "Contact number is required")
    @Pattern(regexp = "^[6-9][0-9]{9}$", message = "Contact number must start with 6, 7, 8, or 9 and be a 10-digit number")
    @Column(name = "contact_number") // Maps to the database column 'contact_number'
    private String contactNumber;

    @Past(message = "Date of birth must be in the past")
    @Column(name = "date_of_birth") // Maps to the database column 'date_of_birth'

    private LocalDate dateOfBirth;

    @NotBlank(message = "Gender is required")
    @Pattern(regexp = "^(Male|Female|Other)$", message = "Gender must be 'Male', 'Female', or 'Other'")
    @Column(name = "gender") // Maps to the database column 'gender'
    private String gender;
    
    @NotBlank(message = "Email ID is required")
    @Email(message = "Email ID must be valid")
    @Size(min = 3, max = 50, message = "Email ID must be between 3 and 50 characters")
    @Column(name = "email_id") // Maps to the database column 'email_id'
    private String emailId;
    
    @NotBlank(message = "Location is required")
    @Pattern(regexp = "^[A-Za-z0-9,\\s.-]+$", message = "Location must contain only alphabetic characters, numbers, commas, spaces, periods, and hyphens")
    @Size(min = 3, max = 50, message = "Location must be between 3 and 50 characters")
    @Column(name = "location") // Maps to the database column 'location'
    private String location;

    
   // @NotBlank(message = "Allergy information is required")
    @Pattern(regexp = "^[A-Za-z,\\s-]+$", message = "Allergy information must contain only alphabetic characters, commas, spaces, and hyphens")
    @Size(min = 4, max = 200, message = "Allergy information must be between 4 and 200 characters")
    @Column(name = "allergies") // Maps to the database column 'allergies'
    private String allergies;

    
    @Pattern(regexp = "^[A-Za-z0-9,\\s.-]+$", message = "Medical history must contain only alphabetic characters, numbers, commas, spaces, periods, and hyphens")
    @Size(min = 3, max = 50, message = "Medical history must be between 3 and 50 characters")
    @Column(name = "medical_history") // Maps to the database column 'medical_history'
    private String medicalHistory;

    @Pattern(regexp = "^[A-Za-z,\\s.-]+$", message = "Medication information must contain only alphabetic characters, commas, spaces, periods, and hyphens")
    @Size(min = 4, max = 50, message = "Medication information must be between 4 and 50 characters")
    @Column(name = "medications") // Maps to the database column 'medications'
    private String medications;

    @Pattern(regexp = "^[A-Za-z0-9,\\s.-]+$", message = "Treatment must contain only alphabetic characters, numbers, commas, spaces, periods, and hyphens")
    @Size(min = 3, max = 50, message = "Treatment must be between 3 and 50 characters")
    @Column(name = "treatments") // Maps to the database column 'treatments'
    private String treatments;

    

    //@NotBlank(message = "Other information is required")
    @Pattern(regexp = "^[A-Za-z0-9,\\s.-]+$", message = "Other information must contain only alphabetic characters, numbers, commas, spaces, periods, and hyphens")
    @Size(min = 3, max = 50, message = "Other information must be between 3 and 50 characters")
    @Column(name = "others") // Maps to the database column 'others'
    private String others;

    @Column(name = "status") // Maps to the database column 'status'
    private String status;

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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

    // Getters and Setters
}
