package com.hms.controller;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hms.entities.Appointment;
import com.hms.entities.Doctor;
import com.hms.entities.Patient;

import jakarta.websocket.server.PathParam;
@Controller
public class AppointmentController {
    
    @Autowired
    private RestTemplate restTemplate;
    
    
    private static final String BASE_URL = "http://localhost:7220"; // URL of the backend API

    Doctor docSession=null;

    Patient patSession=null;
   
    String role = "admin";
    
    @ModelAttribute
    public void getDoc(@SessionAttribute(name = "docObj", required = false) Doctor docObj) {
    	if (docObj != null) {
	    	System.out.println("SESSSSSIIOOOOOON  "+docObj+"  "+docObj.getDoctorId());
	    	docSession = docObj;	    	
    	}    	
    }
    
    @ModelAttribute
    public void getPatient(@SessionAttribute(name = "patObj", required = false) Patient patObj) {
    	if (patObj != null) {
	    	System.out.println("SESSSSSIIOOOOOON  "+patObj+"  "+patObj.getPatientId());
	    	patSession = patObj;
	    	
    	}    	
    }

    @ModelAttribute
    public void getRole(@SessionAttribute(name = "role", required = false) String userRole) {
    	if (userRole != null) {
	    	System.out.println("SESSSSSIIOOOOOON  "+role);
	    	role = userRole;
	    	
    	}    	
    }

    
    @GetMapping("/AppointmentHome")
    public String home(Model model) {
        return "homeappointment";
    }
    
    
    public List<Doctor> getDocList(){
    	String backendUrl = BASE_URL + "/doctors/getAll";
        ResponseEntity<List<Doctor>> response = restTemplate.exchange(
                backendUrl, 
                HttpMethod.GET, 
                null, 
                new ParameterizedTypeReference<List<Doctor>>() {}
        );
        List<Doctor> doctors = response.getBody();
        
    	return doctors;
    }
    @GetMapping("/AppointmentForm")
    public String addAppointmentForm(Model model) {
       
    	List<Doctor> doctors = getDocList();
        model.addAttribute("doctors", doctors);
        
        if (!model.containsAttribute("appointment")) {
            model.addAttribute("appointment", new Appointment());
        }

        return "Appointment";
    }

    @PostMapping("/bookAppointment")
    public String handleBookAppointment(@RequestParam("doctorId") int doctorId, 
    								@RequestParam("patientId") int patientId, @Validated  @ModelAttribute("appointment") Appointment appointment, BindingResult result,                                          
                                        Model model, RedirectAttributes redirectAttributes)         
	{
    	try {
    		
    	RestTemplate restTemplate = new RestTemplate();
            String appointmentUrl = BASE_URL + "/api/appointments/bookAppointment/"+doctorId+"/"+patientId;
            ResponseEntity<Appointment> response = restTemplate.postForEntity(appointmentUrl, appointment, Appointment.class);
            Appointment appObj= response.getBody();
            model.addAttribute("message", "Appointment has been successfully scheduled and  your appointment id is "+ appObj.getAppointmentId() );
            List<Doctor> doctors = getDocList();
            model.addAttribute("doctors", doctors);
            
            return "Appointment";
        } catch (HttpClientErrorException.BadRequest e) {
        	e.printStackTrace();
            Map<String, String> errors = null;
            try {
                errors = new ObjectMapper().readValue(
                    e.getResponseBodyAsString(), new TypeReference<Map<String, String>>() {}
                );
                
            } catch (JsonProcessingException ex) {
                ex.printStackTrace(); // Log the error
            }

            if (errors != null) {
                for (Map.Entry<String, String> entry : errors.entrySet()) {
                    String field = entry.getKey();
                    String errorMsg = entry.getValue();
                    result.rejectValue(field, "", errorMsg); // Bind backend errors to form fields
                }
            }
            
            List<Doctor> doctors = getDocList();
            model.addAttribute("doctors", doctors);
            
            model.addAttribute("appointment", appointment);
            
            return "Appointment";
        }
    	catch (HttpClientErrorException.NotFound e) {
    		e.printStackTrace();
            Map<String, String> errors=null;;
            try {
                errors = new ObjectMapper().readValue(
                    e.getResponseBodyAsString(), new TypeReference<Map<String, String>>() {}
                );
               
            } catch (JsonProcessingException | IllegalArgumentException ex) {
                ex.printStackTrace(); // Log the error
                errors = new HashMap<>();
                errors.put("message", "An unexpected error occurred while processing your request.");
            }
            model.addAttribute("appointment", appointment);
            model.addAttribute("message", errors.get("message")); 
         
            
           
    	}
    	 catch (HttpClientErrorException e) {
    		 e.printStackTrace();
             model.addAttribute("appointment", appointment);
            
    	}
    	catch (HttpServerErrorException e) {
    		e.printStackTrace();
            Map<String, String> errors=null;;
            try {
                errors = new ObjectMapper().readValue(
                    e.getResponseBodyAsString(), new TypeReference<Map<String, String>>() {}
                );
               
            } catch (JsonProcessingException | IllegalArgumentException ex) {
                ex.printStackTrace(); 
                errors = new HashMap<>();
                errors.put("message", "An unexpected error occurred while processing your request.");
            }
            System.out.println(errors.get("message"));
            model.addAttribute("message", errors.get("message")); 
            
        }
    	
    	List<Doctor> doctors = getDocList();
        model.addAttribute("doctors", doctors);
        return "Appointment";
         
    }
    
    @GetMapping("/viewAppointmentByIdForm")
    public String viewAppointmentByIdForm() {
    	return "ViewAppointmentByIdForm";
    }
    
    
    @GetMapping("/viewAppointmentById")
    public String viewAppointmentById(@RequestParam("appointmentId") int appointmentId, Model model) {
        String backendUrl = BASE_URL + "/api/appointments/" + appointmentId;
        try {
            Appointment appointment = restTemplate.getForObject(backendUrl, Appointment.class);
            if (appointment != null) {
                model.addAttribute("appointment", appointment);
            } 
        } 
        catch (HttpClientErrorException.NotFound e) {
            model.addAttribute("errorMessage", "Appointment with ID " + appointmentId + " not found.");
        } 
        catch (Exception e) {
        	e.printStackTrace();
            model.addAttribute("errorMessage", "Error fetching appointment: " + e.getMessage());
        }

        return "ViewAppointmentByIdForm";
    }
    
    @GetMapping("/rescheduleGetAppIdForm")
    public String rescheduleGetAppIdForm() {
    	return "rescheduleGetAppIdForm";
    }
    
    @GetMapping("/getAppointmentById")
    public String getAppointmentById(@RequestParam("appointmentId") int appointmentId, Model model) {
    	
        String backendUrl = BASE_URL + "/api/appointments/" + appointmentId;
        try {
            Appointment appointment = restTemplate.getForObject(backendUrl, Appointment.class);
            if (appointment != null) {
                System.out.println(appointment.getAppointmentDate());
                model.addAttribute("appointment", appointment);
                model.addAttribute("id", appointmentId);
            }
        } 
        catch (HttpClientErrorException.NotFound e) {
            model.addAttribute("errorMessage", "Appointment with ID " + appointmentId + " not found.");
            return "rescheduleGetAppIdForm";
        } 
        catch (Exception e) {
        	e.printStackTrace();
            model.addAttribute("errorMessage", "Error fetching appointment: " + e.getMessage());
        }

        return "rescheduleAppointment";
    }
 
    
    @PostMapping("/rescheduleAppointment")
    public String rescheduleAppointment(@PathParam("appointmentId") int appointmentId, 
                                        @RequestParam LocalDate newDate, 
                                        @RequestParam LocalTime newTime, 
                                        Model model) {
        String url = BASE_URL + "/api/appointments/reschedule/"+ appointmentId+"?newDate=" + newDate + "&newTime=" + newTime;
        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url, HttpMethod.PUT, null, String.class
            );
            model.addAttribute("message", response.getBody());
            
        } catch (HttpClientErrorException | HttpServerErrorException e) {
        	Map<String, String> errors=null;;
            try {
                errors = new ObjectMapper().readValue(
                    e.getResponseBodyAsString(), new TypeReference<Map<String, String>>() {}
                );
               
            } catch (JsonProcessingException | IllegalArgumentException ex) {
                ex.printStackTrace(); // Log the error
                errors = new HashMap<>();
                errors.put("message", "An unexpected error occurred while processing your request.");
            }
            model.addAttribute("message", errors.get("message")); 
            model.addAttribute("id",appointmentId);
            return "rescheduleAppointment";
        }
        
        catch(Exception e) {
        	e.printStackTrace();
        }

        return "/patient/appointmentinfo"; 
    }
    @GetMapping("/cancel")
    public String cancel() {
        return "cancelAppointment"; 
    }
    @PostMapping("/cancelApp")
    public String cancelAppointment(@RequestParam("appointmentId") int appointmentId, Model model) {
        try {
            String cancelUrl = BASE_URL + "/api/appointments/cancel/" + appointmentId;
            ResponseEntity<String> response = restTemplate.exchange(
                    cancelUrl, HttpMethod.PUT, null, String.class
            );

            model.addAttribute("message", "Your appointment with ID " + appointmentId + " has been successfully canceled.");
        } 
        catch (HttpClientErrorException.NotFound e) {
        	Map<String, String> errors=null;;
            try {
                errors = new ObjectMapper().readValue(
                    e.getResponseBodyAsString(), new TypeReference<Map<String, String>>() {}
                );
               
            } catch (JsonProcessingException | IllegalArgumentException ex) {
                ex.printStackTrace(); // Log the error
                errors = new HashMap<>();
                errors.put("message", "An unexpected error occurred while processing your request.");
            }
            model.addAttribute("errorMessage", errors.get("message")); 
            
        } 
        catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("message", "Appointment with ID " + appointmentId + " not found to cancel.");
        }
        return "cancelAppointment";
    }
    
    @GetMapping("/patientsWithAppointmentToday")
    public String getPatientsWithAppointmentToday(Model model) {
        String backendUrl = BASE_URL + "/api/appointments/patientsWithAppointmentCurrentDay";
        try {
            ResponseEntity<List<Patient>> response = restTemplate.exchange(
                backendUrl, 
                HttpMethod.GET, 
                null, 
                new ParameterizedTypeReference<List<Patient>>() {}
            );
            List<Patient> patients = response.getBody();
            model.addAttribute("patients", patients);
        }
        catch (HttpClientErrorException | HttpServerErrorException e) {
            // Handle 404 error specifically
        	Map<String, String> errors=null;;
			try {
				errors = new ObjectMapper().readValue(
				    e.getResponseBodyAsString(), new TypeReference<Map<String, String>>() {});
			
				// Map backend error message to Model
				model.addAttribute("errorMessage", errors.get("message")); //mapname.get(key) ->value
			} catch (Exception e1) {
				e1.printStackTrace();
				model.addAttribute("errorMessage", "Error fetching patients: " + e1.getMessage());
			}
        }
        return "patientsWithAppointments"; 
    }
    
    @GetMapping("/viewAppointmentsForCurrentDate")
    public String viewAppointmentsForCurrentDate(Model model) {
        LocalDate currentDate = LocalDate.now();
        String backendUrl = "http://localhost:7220/api/appointments/appointmentsForDate/" + currentDate.toString();
        
        try {
            ResponseEntity<List<Appointment>> response = restTemplate.exchange(
                    backendUrl, 
                    HttpMethod.GET, 
                    null, 
                    new ParameterizedTypeReference<List<Appointment>>() {}
            );
            List<Appointment> appointments = response.getBody();
            model.addAttribute("appointments", appointments);
        }  catch (HttpClientErrorException | HttpServerErrorException e) {
            // Handle 404 error specifically
        	Map<String, String> errors=null;;
			try {
				errors = new ObjectMapper().readValue(
				    e.getResponseBodyAsString(), new TypeReference<Map<String, String>>() {});
			
				// Map backend error message to Model
				model.addAttribute("errorMessage", errors.get("message")); //mapname.get(key) ->value
		}catch (Exception e1) {
            e1.printStackTrace();
            model.addAttribute("errorMessage", "Error fetching patients: " + e1.getMessage());
        }
     }
        
        return "appointmentsForToday";  
    }
    @PostMapping("/updateAppointment")
    public String updateAppointment(@ModelAttribute("appointment") Appointment appointment, Model model) {
        RestTemplate restTemplate = new RestTemplate();

        try {
            // Send updated data to the backend
            String updateUrl = "http://localhost:7220/api/appointments/" + "/update/" + appointment.getAppointmentId();
            restTemplate.put(updateUrl, appointment);

            model.addAttribute("message", "Appointment updated successfully!");
        } catch (HttpClientErrorException e) {
            // Check for specific HTTP status codes
            if (e.getStatusCode().value() == 404) {
                model.addAttribute("errorMessage", "Appointment ID not found. Please provide a valid ID.");
            } 
            else if (e.getStatusCode().value() == 400 && e.getResponseBodyAsString().contains("cancelled")) {
                model.addAttribute("errorMessage", "Cannot update a cancelled appointment.");
            }
            else {
                model.addAttribute("errorMessage", "Failed to update appointment. Please try again.");
            }
        } catch (Exception ex) {
            model.addAttribute("errorMessage", "An unexpected error occurred. Please try again.");
        }
        return "update-appointment";
    }
    @GetMapping("/updateAppointmentForm")
    public String getUpdateForm(Model model) {
        model.addAttribute("appointment", new Appointment());
        return "update-appointment";
    }
    @GetMapping("/viewAllAppointments")
    public String viewAllAppointments(Model model) {
        // Fetch all appointments
        String backendUrl = BASE_URL + "/api/appointments/all";
        try {
            ResponseEntity<List<Appointment>> response = restTemplate.exchange(
                    backendUrl, 
                    HttpMethod.GET, 
                    null, 
                    new ParameterizedTypeReference<List<Appointment>>() {}
            );
            List<Appointment> appointments = response.getBody();
            model.addAttribute("appointments", appointments);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "Error fetching appointments: " + e.getMessage());
        }
        return "viewAllAppointments"; 
    }
    @GetMapping("/lowConsultationDoctorsReport")
    public String lowConsultationDoctorsReport(
        @RequestParam(value = "startDate", required = false) String startDate, 
        @RequestParam(value = "endDate", required = false) String endDate, 
        Model model) {

        // If the form is just being loaded (no parameters), we can just show the form without doing anything
        if (startDate == null || endDate == null) {
            return "lowConsultationDoctorsReport"; // Just return the view without fetching data yet
        }

        try {
            // Validate the date format (yyyy-MM-dd)
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate start = LocalDate.parse(startDate, formatter);
            LocalDate end = LocalDate.parse(endDate, formatter);

            // Check if the start date is after the end date
            if (start.isAfter(end)) {
                model.addAttribute("errorMessage", "Start date cannot be after end date.");
                return "lowConsultationDoctorsReport"; // Return to the same view with error message
            }

            // Prepare the backend URL with the given date range
            String backendUrl = BASE_URL + "/api/appointments/lowConsultationDoctors?" + "startDate=" + startDate + "&endDate=" + endDate;
            
            // Call the backend API using RestTemplate
            ResponseEntity<List<Doctor>> response = restTemplate.exchange(
                backendUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Doctor>>() {}
            );
            
            // Extract the doctor list from the response
            List<Doctor> doctors = response.getBody();
            
            // Add the list of doctors to the model
            model.addAttribute("doctors", doctors);
            model.addAttribute("startDate", startDate);  // Passing startDate to the view
            model.addAttribute("endDate", endDate);      // Passing endDate to the view
            
        } catch (DateTimeParseException e) {
            model.addAttribute("errorMessage", "Enter Dates");
        } catch (HttpClientErrorException e) {
            // Handle 400 Bad Request errors
            if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                try {
                    String responseBody = e.getResponseBodyAsString();
                    // Log the response body to inspect the format
                    System.out.println("Raw Response Body: " + responseBody);

                    // Try parsing as JSON if it is JSON
                    try {
                        Map<String, String> errorDetails = new ObjectMapper().readValue(responseBody, new TypeReference<Map<String, String>>() {});
                        String errorMessage = errorDetails.getOrDefault("message", "Bad Request: " + e.getMessage());
                        model.addAttribute("errorMessage", errorMessage);
                    } catch (JsonProcessingException jsonEx) {
                        // Handle cases where the response is not valid JSON (e.g., just a plain string)
                        model.addAttribute("errorMessage", "Enter  " + responseBody);
                    }

                } catch (Exception ex) {
                    // Handle any other unforeseen error in processing
                    ex.printStackTrace();
                    model.addAttribute("errorMessage", "Unexpected error: " + ex.getMessage());
                }
            } else {
                model.addAttribute("errorMessage", "Error fetching doctor data: " + e.getMessage());
            }
        }

	    catch (HttpServerErrorException e) {
	            e.printStackTrace();
	            model.addAttribute("errorMessage", "Server error: " + e.getMessage());
	        } catch (Exception e) {
	            e.printStackTrace();
	            model.addAttribute("errorMessage", "Error fetching appointments: " + e.getMessage());
	        }

        // Return the view name that will display the report
        return "lowConsultationDoctorsReport";
    }
    
    @GetMapping("/viewDoctorAppointmentsForm")
    public String viewDoctorAppointmentsForm(Model model) {
        // Add a new map to hold the form inputs if needed
        model.addAttribute("searchCriteria", new HashMap<String, String>());
        return "viewDoctorAppointmentsForm"; // This corresponds to the form view
    }
    @PostMapping("/viewDoctorAppointments")
    public String viewDoctorAppointments(@RequestParam("doctorId") int doctorId,
                                          @RequestParam("startDate") String startDate,
                                          @RequestParam("endDate") String endDate,
                                          Model model) {
        try {
            String backendUrl = BASE_URL + "/api/appointments/doctor/" + doctorId +
                                "?startDate=" + startDate + "&endDate=" + endDate;

            ResponseEntity<List<Appointment>> response = restTemplate.exchange(
                    backendUrl,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Appointment>>() {}
            );

            List<Appointment> appointments = response.getBody();
            model.addAttribute("appointments", appointments);
        } 
        catch (HttpClientErrorException.NotFound e) {
            model.addAttribute("errorMessage", "Doctor ID not found. Enter a valid ID.");
            return "viewDoctorAppointmentsForm"; // Return the same page
        }
        catch (HttpClientErrorException | HttpServerErrorException e) {
            // Handle 404 error specifically
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
        catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "Error fetching appointments: " + e.getMessage());
        }

        return "viewDoctorAppointments"; // This corresponds to the result view
    }

    

}
