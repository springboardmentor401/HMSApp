package com.hms.controller;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
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
    
    @GetMapping("/AppointmentHome")
    public String home(Model model) {
        return "homeappointment";
    }
    
    
    public List<Doctor> getDocList(){
    	String backendUrl = BASE_URL + "/doctors";
        ResponseEntity<List<Doctor>> response = restTemplate.exchange(
                backendUrl, 
                HttpMethod.GET, 
                null, 
                new ParameterizedTypeReference<List<Doctor>>() {}
        );
        List<Doctor> doctors = response.getBody();
        System.out.println(response.getBody());
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
            ResponseEntity<String> response = restTemplate.postForEntity(appointmentUrl, appointment, String.class);
            model.addAttribute("message", "Appointment has been successfully scheduled");
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
            System.out.println(errors.get("message"));
            model.addAttribute("message", errors.get("message")); 
            model.addAttribute("id",appointmentId);
            return "rescheduleAppointment";
        }
        
        catch(Exception e) {
        	e.printStackTrace();
        }

        return "home"; 
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
            model.addAttribute("errorMessage", "Appointment with ID " + appointmentId + " not found.");
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
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "Error fetching patients: " + e.getMessage());
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
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "Error fetching appointments: " + e.getMessage());
        }
        
        return "appointmentsForToday";  
    }



    

}
