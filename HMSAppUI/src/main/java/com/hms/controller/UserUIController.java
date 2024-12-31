package com.hms.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.web.client.HttpClientErrorException;

import com.hms.entities.Doctor;
import com.hms.entities.Patient;
import com.hms.entities.UserInfo;

import jakarta.servlet.http.HttpSession;

@Controller
public class UserUIController {

    
    private String apiBaseUrl = "http://localhost:7220";

    @Autowired
    private RestTemplate restTemplate;
    
    @GetMapping("/")
    public String showMainPage() {
        return "mainpage";
    }
    
    @GetMapping("/loginPage")
    public String showLoginPage(Model model) {
        model.addAttribute("userInfo", new UserInfo());
        return "login";
    }

//    
    
    @PostMapping("/login")
    public String loginUser(@ModelAttribute  UserInfo userInfo, BindingResult result,Model model, HttpSession session) {
        String apiUrl = apiBaseUrl + "/api/users/login";
        System.out.println(apiUrl);
        try {
            // Send login request to REST API
            UserInfo res = restTemplate.postForObject(apiUrl, userInfo, UserInfo.class);

            System.out.println(res.getUserName());
            if (res != null) {
                // Redirect to a dashboard or home page on successful login
            
            	session.setAttribute("role","");
            	session.setAttribute("docObj", null);
            	session.setAttribute("patObj", null);
                
                session.setAttribute("userObj", res.getUserName());
            	if(res.getRole().equalsIgnoreCase("admin")) {
            		
                	session.setAttribute("role","admin");
                        		
                	return "/admin/admin";
                }
                else if(res.getRole().equalsIgnoreCase("doctor")) {
                	
                	System.out.println("http://localhost:7220/doctors/fetchByUserName/" + res.getUserName());
                	ResponseEntity<Doctor> resp = restTemplate.getForEntity(apiBaseUrl+"/doctors/fetchByUserName/" + res.getUserName(), Doctor.class);
                	Doctor docObj = resp.getBody();
                	session.setAttribute("role","doctor");
                	session.setAttribute("docObj", docObj);
//                	model.addAttribute("role","doctor");
//                	model.addAttribute("docId", docObj.getDoctorId());
                	return "doctorhome";
                }
                else if (res.getRole().equalsIgnoreCase("patient")) {
                    System.out.println("HELLLLLLLLLOOOOOOOOOOOOO");
                    ResponseEntity<Patient> patObj = restTemplate.getForEntity(apiBaseUrl + "/api/patient/fetchByUserName/" + res.getUserName(), Patient.class);
                    
                    session.setAttribute("role", "patient");
                    session.setAttribute("patObj", patObj.getBody());
                    
                    return "/patient/patient";  // Or redirect as needed
                }
                else
                {
                	System.out.println("hjejkh");
                }

            }
            else 
            {
                model.addAttribute("error", "Invalid credentials!");
                return "login";
            }
        } catch (HttpClientErrorException | HttpServerErrorException e) {
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
        return "login";				
    }

    @GetMapping("/dashboard")
    public String showDashboard() {
        return "dashboard"; // Create a Thymeleaf template `dashboard.html` for this
    }
    
    @GetMapping("/logout")
    public String logout(HttpSession session) {
    	session.setAttribute("patObj", null);
        
    	session.setAttribute("docObj", null);
    	session.setAttribute("role", "");
    	
        session.invalidate();
    	return "logout"; // Create a Thymeleaf template `dashboard.html` for this
    }
    
    @GetMapping("/admin")
    public String getAdminHome() {
    	return "admin/admin";
    }
    
    @GetMapping("/admin/doctorinfo")
    public String getAdminDocInfo() {
    	return "admin/doctorinfo";
    }
    
    @GetMapping("/admin/patientinfo")
    public String getAdminPatientInfo() {
    	return "admin/patientinfo";
    }
    
    @GetMapping("/admin/appointmentinfo")
    public String getAdminAppointmentinfoInfo() {
    	return "admin/appointmentinfo";
    }
    
    @GetMapping("/admin/billinfo")
    public String getAdminBillInfo() {
    	return "admin/billinfo";
    }
    
    @GetMapping("/admin/paymentinfo")
    public String getAdminPaymentInfo() {
    	return "admin/paymentinfo";
    }
    
    @GetMapping("/patient")
    public String getPatientHome() {
    	return "patient/patient";
    }
    
    @GetMapping("/patient/doctorinfo")
    public String getPatientDocInfo() {
    	return "patient/doctorinfo";
    }
    
    @GetMapping("/patient/patientinfo")
    public String getPatientPatientInfo() {
    	return "patient/patientinfo";
    }
    
    @GetMapping("/patient/appointmentinfo")
    public String getPatientAppointmentinfoInfo() {
    	return "patient/appointmentinfo";
    }
    
    @GetMapping("/patient/billinfo")
    public String getPatientBillInfo() {
    	return "patient/billinfo";
    }
    
    @GetMapping("/patient/paymentinfo")
    public String getPatientPaymentInfo() {
    	return "patient/paymentinfo";
    }
    
}

//@GetMapping("/doctors/addDoctorForm")
//public String showAddDoctorForm(Model model) {
//  model.addAttribute("doctor", new Doctor());
//  return "addDoctor";
//}
//
//// Adjust the POST mapping to match the correct form action
//
//@PostMapping("/doctors/addDoctor")
//
//public String addDoctor(@ModelAttribute Doctor doctor, BindingResult result, Model model) {
//
//  System.out.println(doctor.getDoctorId()+" "+doctor.getDoctorName());  	
//
//	try {
//
//      // Sending POST request to backend API to add the doctor
//		String BASE_URL = "http://localhost:7220";
//	    ResponseEntity<String> response = this.restTemplate.postForEntity(    	    		
//              BASE_URL + "/doctor/addDoctor", doctor, String.class);
//      model.addAttribute("message", "Doctor added successfully: " + response.getBody());
//      model.addAttribute("userInfo", new UserInfo());
//      return "login";
//
//	} 
//
//  catch (HttpClientErrorException.BadRequest e) {
//
//  	e.printStackTrace();
//      // Parse and display validation errors from backend
//
//      Map<String, String> errors=null;;
//
//				try {
//
//					errors = new ObjectMapper().readValue(
//
//					    e.getResponseBodyAsString(), new TypeReference<Map<String, String>>() {});
//
//				} catch (JsonMappingException e1) {
//
//					// TODO Auto-generated catch block
//
//					e1.printStackTrace();
//
//				} catch (JsonProcessingException e1) {
//
//					// TODO Auto-generated catch block
//
//					e1.printStackTrace();
//
//				}
//
//			// Map backend errors to BindingResult				
//
//			for(Map.Entry<String, String> entryset : errors.entrySet()) {
//
//				String field = entryset.getKey();
//
//				String errorMsg = entryset.getValue();							
//
//				result.rejectValue(field,"",errorMsg);
//			}
//
//			return "addDoctor";
//  }
//	catch (HttpClientErrorException | HttpServerErrorException e) {
//      // Parse and display error message from backend
//  	Map<String, String> errors=null;;
//		try {
//			errors = new ObjectMapper().readValue(
//			    e.getResponseBodyAsString(), new TypeReference<Map<String, String>>() {});
//		
//			// Map backend error message to Model
//			model.addAttribute("errorMessage", errors.get("message")); //mapname.get(key) ->value
//		} catch (JsonProcessingException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		return "addDoctor";			
//  }
//	
//
//}
