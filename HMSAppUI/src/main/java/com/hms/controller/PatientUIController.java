
 


package com.hms.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.hms.entities.Patient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;


@Controller
public class PatientUIController {

    @Autowired
    private RestTemplate restTemplate;

    private final String BASE_URL = "http://localhost:7211";

    @GetMapping("/")
    public String home() {
        return "home";
    }
    /*

    @GetMapping("/addPatientForm")
    public String addPatientForm(Model model) {
        model.addAttribute("patient", new Patient());
        return "addPatient"; // Returns the add-patient.html template
    }*/
    @GetMapping("/addPatientForm")
    public String addPatientForm(Model model) {
        model.addAttribute("patient", new Patient());
        return "addPatient"; // Returns the addPatient.html template
    }
    /*
    // Handle form submission to add a new patient
    @PostMapping("/addPatients")
    public String addPatient(@ModelAttribute Patient patient, Model model) {
        ResponseEntity<Patient> response = restTemplate.postForEntity(BASE_URL + "/api/patient/addPatient", patient, Patient.class);
        model.addAttribute("message", "Patient added successfully: " + response.getBody().getPatientName());
        return "home";
    }*/
    @PostMapping("/addPatients")
    public String handleAddDoctor(@ModelAttribute("patient") Patient patient, BindingResult result, Model model) {
        // Use RestTemplate to call backend API
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<Patient> response = restTemplate.postForEntity(
                BASE_URL+"/api/patient/addPatient",
                patient,
                Patient.class
            );
            
//            model.addAttribute("message", "Patient added successfully: " + response.getBody());

            Patient patientRes = response.getBody();
            model.addAttribute("message", "Patient added successfully with id " + patientRes.getPatientId());
            return "home";
            
        } catch (HttpClientErrorException e) {
            // Parse and display validation errors from backend
            Map<String, String> errors=null;;
					try {
						errors = new ObjectMapper().readValue(
						    e.getResponseBodyAsString(), new TypeReference<Map<String, String>>() {});
					} catch (JsonMappingException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (JsonProcessingException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
//				
			
				// Map backend errors to BindingResult				
				for(Map.Entry<String, String> entryset : errors.entrySet()) {
					String field = entryset.getKey();
					String errorMsg = entryset.getValue();							
					result.rejectValue(field,"",errorMsg);
				}
				return "addPatient";
				
			
        }
        
    }

    // Method to view all patients
    @GetMapping("/viewAllPatients")
    public String viewAllPatients(Model model) {
        //List<Patient> patients = patientService.viewAllPatients(); // Fetch all patients
        ResponseEntity<List> response=restTemplate.getForEntity(BASE_URL +"/api/patient/viewAllPatients",List.class);
    	model.addAttribute("patients",response.getBody()); // Add the patients to the model
        return "viewAllPatients"; // Return the view name
    }
    @GetMapping("/viewPatientByIdForm")
    public String viewPatientByIdForm()
    {
    	return "viewPatientByIdForm";
    }
    /*
    @GetMapping("/viewPatientById")
    public String viewPatientById(@RequestParam("patientId") int patientId, Model model) {
        try {
            ResponseEntity<Patient> response = restTemplate.getForEntity(BASE_URL + "/api/patient/viewPatientById/" + patientId, Patient.class);
            if (response.getBody() != null) {
                model.addAttribute("patient", response.getBody());
            } else {
                model.addAttribute("error", "Patient not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Patient not found");
        }
        return "viewPatientById";
    }*/
    @GetMapping("/viewPatientById")
    public String viewPatientById(@RequestParam("patientId") int patientId, Model model) {
        // Use RestTemplate to call backend API
    	
        RestTemplate restTemplate = new RestTemplate();
        try {
        
        
        ResponseEntity<Patient> response = restTemplate.getForEntity(
                BASE_URL+"/api/patient/viewPatientById/"+patientId,                
                Patient.class
            );

            // Extract the Doctor object from the ResponseEntity
        Patient patient = response.getBody();
            
            // If the doctor is found, add it to the model
            model.addAttribute("patient", patient);
            
        } catch (HttpClientErrorException e) {
            // Parse and display error message from backend
            Map<String, String> errors=null;;
			try {
				errors = new ObjectMapper().readValue(
				    e.getResponseBodyAsString(), new TypeReference<Map<String, String>>() {});
			
				// Map backend error message to Model
				model.addAttribute("errorMessage", errors.get("message")); //mapname.get(key) ->value
			} catch (JsonProcessingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        }
        return "viewPatientById";
    }
 // Handle viewing patients by name
    @GetMapping("/viewPatientByName")
    public String viewPatientByName(@RequestParam("name") String name, Model model) {
        ResponseEntity<List> response = restTemplate.getForEntity(BASE_URL + "/api/patient/viewPatientByName/" + name, List.class);
        if (response.getBody() != null && !response.getBody().isEmpty()) {
            model.addAttribute("patients", response.getBody());
        } else {
            model.addAttribute("error", "No patients found with the name: " + name);
        }
        return "viewPatientByName"; // Returns the viewPatientByName.html template
    }

    
   
    @GetMapping("/viewPatientByNameForm")
    public String viewPatientByNameForm() {
        return "viewPatientByNameForm"; // Returns the viewPatientByNameForm.html template
    }

    @GetMapping("/updatePatientForm")
    public String updatePatientForm() {
        return "updatePatientForm"; // Returns the updatePatientForm.html template
    }

    // Handle the form submission to fetch and display the patient data
    @PostMapping("/updatePatient")
    public String updatePatient(@RequestParam("patientId") int patientId, Model model) {
        try {
            // Fetch the patient by ID
            ResponseEntity<Patient> response = restTemplate.getForEntity(BASE_URL + "/api/patient/viewPatientById/" + patientId, Patient.class);
            if (response.getBody() != null) {
                model.addAttribute("patient", response.getBody());
                return "updatePatient"; // Returns the updatePatient.html template with patient data
            } else {
                model.addAttribute("error", "Patient not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Patient not found");
        }
        return "updatePatientForm"; // Return to the form if patient not found
    }

    // Handle the update of the patient details
    @RequestMapping(value = "/update-Patient/{id}", method = RequestMethod.PUT)
    public String updatePatientInfo(@ModelAttribute Patient patient, @PathVariable int id, Model model) {
        try {
            // Make a PATCH request to update the patient data
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Patient> entity = new HttpEntity<>(patient, headers);
            ResponseEntity<Void> response = restTemplate.exchange(BASE_URL + "/api/patient/update-Patient/" + id, HttpMethod.PATCH, entity, Void.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                model.addAttribute("message", "Patient updated successfully: " + patient.getPatientName());
                return "home"; // Redirect to home after successful update
            } else {
                model.addAttribute("error", "Failed to update patient");
            }
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Error updating patient");
        }
        return "updatePatient"; // Return to the updatePatient page if something goes wrong
    }
    @GetMapping("/viewPatientByMedicalHistory")
    public String viewPatientByMedicalHistory(@RequestParam("history") String medicalHistory, Model model) {
        // Call the backend API to fetch patients based on medical history
        ResponseEntity<List> response = restTemplate.getForEntity(BASE_URL + "/api/patient/viewPatientByMedicalHistory/" + medicalHistory, List.class);
        
        // Check if the response contains patients and update the model accordingly
        if (response.getBody() != null && !response.getBody().isEmpty()) {
            model.addAttribute("patients", response.getBody());
        } else {
            model.addAttribute("error", "No patients found with the medical history: " + medicalHistory);
        }

        // Return the template name to display the results
        return "viewPatientByMedicalHistory"; // Returns the viewPatientByMedicalHistory.html template
    }

    @GetMapping("/viewPatientByMedicalHistoryForm")
    public String viewPatientByMedicalHistoryForm() {
        return "viewPatientByMedicalHistoryForm"; // Returns the viewPatientByMedicalHistoryForm.html template
    }

    

    @GetMapping("/viewPatientsByDoctorAndDate")
    public String viewPatientsByDoctorAndDate(@RequestParam int doctorId, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate appDate, Model model) {
        ResponseEntity<List> response = restTemplate.getForEntity(
                BASE_URL + "/api/patient/by-doctor-and-date?doctorId=" + doctorId + "&appDate=" + appDate, List.class);
        
        if (response.getStatusCode() == HttpStatus.OK) {
            model.addAttribute("patients", response.getBody());
        } else {
            model.addAttribute("error", "No patients found for doctorId: " + doctorId + " on date: " + appDate);
        }
        
        return "viewPatientsByDoctorAndDate"; // This should be the name of the template to display the results
    }
    @GetMapping("/viewPatientsByDoctorAndDateForm")
    public String viewPatientsByDoctorAndDateForm() {
        return "viewPatientsByDoctorAndDateForm"; // This should match the name of the form template
    }
}






