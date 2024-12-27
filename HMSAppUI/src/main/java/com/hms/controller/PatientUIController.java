package com.hms.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hms.entities.Patient;

@Controller
public class PatientUIController {

    @Autowired
    private RestTemplate restTemplate;

    private final String BASE_URL = "http://localhost:7220";

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/addPatientForm")
    public String addPatientForm(Model model) {
        model.addAttribute("patient", new Patient());
        return "addPatient";
    }

    @PostMapping("/addPatients")
    public String handleAddPatient(@ModelAttribute("patient") Patient patient, BindingResult result, Model model) {
        try {
            ResponseEntity<Patient> response = restTemplate.postForEntity(
                BASE_URL + "/api/patient/addPatient",
                patient,
                Patient.class
            );

            Patient patientRes = response.getBody();
            model.addAttribute("message", "Patient added successfully with ID " + patientRes.getPatientId());
            return "home";
        } catch (HttpClientErrorException e) {
            Map<String, String> errors = parseBackendErrors(e);
            if (errors != null) {
                errors.forEach((field, errorMsg) -> result.rejectValue(field, "", errorMsg));
            }
            model.addAttribute("error", "Failed to add patient. Please correct the errors.");
            return "addPatient";
        }
    }

    @GetMapping("/viewAllPatients")
    public String viewAllPatients(Model model) {
        try {
            ResponseEntity<List> response = restTemplate.getForEntity(BASE_URL + "/api/patient/viewAllPatients", List.class);
            model.addAttribute("patients", response.getBody());
        } catch (Exception e) {
            model.addAttribute("error", "Unable to fetch patients. Please try again later.");
        }
        return "viewAllPatients";
    }

    @GetMapping("/viewPatientByIdForm")
    public String viewPatientByIdForm() {
        return "viewPatientByIdForm";
    }

    @GetMapping("/viewPatientById")
    public String viewPatientById(@RequestParam("patientId") int patientId, Model model) {
        try {
            ResponseEntity<Patient> response = restTemplate.getForEntity(
                BASE_URL + "/api/patient/viewPatientById/" + patientId,
                Patient.class
            );
            model.addAttribute("patient", response.getBody());
            return "viewPatientById";
            
        } 
        catch (HttpClientErrorException e) {
            Map<String, String> errors = parseBackendErrors(e);
            model.addAttribute("error", errors != null ? errors.get("message") : "Patient not found.");
            return "viewPatientByIdForm";
        }
        //return "viewPatientByIdForm";
    }

    @GetMapping("/viewPatientByNameForm")
    public String viewPatientByNameForm() {
        return "viewPatientByNameForm";
    }

    @GetMapping("/viewPatientByName")
    public String viewPatientByName(@RequestParam("name") String name, Model model) {
        try {
            ResponseEntity<List> response = restTemplate.getForEntity(BASE_URL + "/api/patient/viewPatientByName/" + name, List.class);
            model.addAttribute("patients", response.getBody());
            return "viewPatientByName";
        } catch (Exception e) {
            model.addAttribute("error", "No patients found with the name: " + name);
            return "viewPatientByNameForm";
        }
        //return "viewPatientByName";
    }

    @GetMapping("/updatePatientForm")
    public String updatePatientForm(Model model) {
        model.addAttribute("patient", new Patient());
        return "updatePatientForm";
    }

    @PostMapping("/fetchPatientForUpdate")
    public String fetchPatientForUpdate(@RequestParam("patientId") int patientId, Model model) {
        try {
            ResponseEntity<Patient> response = restTemplate.getForEntity(
                BASE_URL + "/api/patient/viewPatientById/" + patientId,
                Patient.class
            );
            model.addAttribute("patient", response.getBody());
            return "updatePatient";
        } catch (Exception e) {
            model.addAttribute("error", "Patient not found with ID: " + patientId);
            return "updatePatientForm";
        }
    }

    @PostMapping("/updatePatients")
    public String updatePatient(@ModelAttribute("patient") Patient patient, BindingResult result, Model model) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Patient> request = new HttpEntity<>(patient, headers);

            // Ensure the URL is correctly formed with patientId as a path variable
            ResponseEntity<Patient> response = restTemplate.exchange(
                BASE_URL + "/api/patient/updatePatient/" + patient.getPatientId(), // Correct path variable used here
                HttpMethod.PUT,
                request,
                Patient.class
            );

            model.addAttribute("message", "Patient updated successfully: " + response.getBody().getPatientName());
            return "home";
        } catch (HttpClientErrorException e) {
            Map<String, String> errors = parseBackendErrors(e);
            if (errors != null) {
                errors.forEach((field, errorMsg) -> result.rejectValue(field, "", errorMsg));
            }
            model.addAttribute("error", "Failed to update patient. Please correct the errors.");
            return "updatePatient";
        }
    }

    @GetMapping("/viewPatientByMedicalHistoryForm")
    public String viewPatientByMedicalHistoryForm() {
        return "viewPatientByMedicalHistoryForm";
    }

    @GetMapping("/viewPatientByMedicalHistory")
    public String viewPatientByMedicalHistory(@RequestParam("history") String medicalHistory, Model model) {
        try {
            ResponseEntity<List> response = restTemplate.getForEntity(BASE_URL + "/api/patient/viewPatientByMedicalHistory/" + medicalHistory, List.class);
            model.addAttribute("patients", response.getBody());
            return "viewPatientByMedicalHistory";
        } catch (Exception e) {
            model.addAttribute("error", "No patients found with the medical history: " + medicalHistory);
            return "viewPatientByMedicalHistoryForm";
        }
        //return "viewPatientByMedicalHistory";
    }

    @GetMapping("/viewPatientsByDoctorAndDateForm")
    public String viewPatientsByDoctorAndDateForm() {
        return "viewPatientsByDoctorAndDateForm";
    }

    @GetMapping("/viewPatientsByDoctorAndDate")
    public String viewPatientsByDoctorAndDate(
            @RequestParam(name = "doctorId") int doctorId,
            @RequestParam(name = "appDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate appDate,
            Model model) {
        try {
            ResponseEntity<List> response = restTemplate.getForEntity(
                    BASE_URL + "/api/patient/by-doctor-and-date?doctorId=" + doctorId + "&appDate=" + appDate, 
                    List.class);

            model.addAttribute("patients", response.getBody());
            return "viewPatientsByDoctorAndDate";
        } catch (Exception e) {

            model.addAttribute("error", 
                    "No patients found for doctorId: " + doctorId + " on date: " + appDate + ". Error: " + e.getMessage());
            return "viewPatientsByDoctorAndDateForm";
        }

          
        }
        //return "viewPatientsByDoctorAndDate";




    @GetMapping("/no-show-patients")
    public String getNoShowPatients(Model model) {
        try {
            ResponseEntity<List<Patient>> response = restTemplate.exchange(
                BASE_URL + "/api/patient/no-show-report",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Patient>>() {}
            );
            model.addAttribute("noShowPatients", response.getBody());
        } catch (Exception e) {
            model.addAttribute("error", "Unable to fetch no-show patients report.");
        }
        return "noShowPatients";
    }

    private Map<String, String> parseBackendErrors(HttpClientErrorException e) {
        try {
            return new ObjectMapper().readValue(e.getResponseBodyAsString(), new TypeReference<Map<String, String>>() {});
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
