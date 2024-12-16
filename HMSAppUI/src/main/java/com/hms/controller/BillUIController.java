package com.hms.controller;

import java.util.List;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.core.type.TypeReference;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hms.entities.Bill;


@Controller
public class BillUIController {

    @Autowired
    private RestTemplate restTemplate;

    private final String BASE_URL = "http://localhost:7211"; // Base URL for the backend API

    @GetMapping("/") // Renders the home page
    public String home() {
        return "home"; // This will render home.html
    }

    @GetMapping("/addBillForm") // Renders the add bill page
    public String showAddBillPage(Model model) {
        model.addAttribute("bill", new Bill());
      
        return "billAdd"; // This will render billAdd.html
    }

    @PostMapping("/addBill/{appointmentId}")
    public String addBill(@PathVariable("appointmentId") Long appointmentId, 
                          @ModelAttribute Bill bill,BindingResult result, 
                          Model model) {

        // Ensure the appointmentId is correctly set in the bill object if necessary
        // bill.setAppointmentId(appointmentId); // If the bill has an appointmentId field
    	// Log the incoming bill object
        System.out.println("Received bill object: " + bill);
        System.out.println("Appointment ID: " + appointmentId);
        System.out.println("Consultation fee: " + bill.getConsultationFees());

        // Prepare the URL for the API call
        String url = BASE_URL + "/api/bills/generateBill/" + appointmentId;

        // Remove the nested appointment object to avoid sending it in the request payload
        bill.setAppointment(null); // Remove the appointment object if it's nested

        try {
        ResponseEntity<Bill> response = restTemplate.postForEntity(url, bill, Bill.class);

        // Check if the response is successful
        if (response.getStatusCode().is2xxSuccessful()) {
            model.addAttribute("successMessage", "Bill added successfully with ID: " + response.getBody().getBillId());
        } else {
            model.addAttribute("errorMessage", "Error occurred while adding the bill. Status code: " + response.getStatusCodeValue());
        }
    } catch (HttpClientErrorException e) {
        // Parse and display validation errors from backend
        Map<String, String> errors = null;
        try {
            // Parse the response body containing validation errors
            errors = new ObjectMapper().readValue(
                e.getResponseBodyAsString(), new TypeReference<Map<String, String>>() {}
            );

            // Map backend errors to BindingResult
            for (Map.Entry<String, String> entry : errors.entrySet()) {
                String field = entry.getKey();
                String errorMsg = entry.getValue();

                // Add errors to BindingResult
                result.rejectValue(field, "", errorMsg);
            }
        } catch (JsonProcessingException e1) {
            e1.printStackTrace();
        }
    }
        return "billAdd"; // Return the appropriate view (e.g., a success message or form with errors)
    }


    @GetMapping("/viewBills")
    public String viewBills(Model model) {
        String url = BASE_URL + "/api/bills/viewAllBills"; // Endpoint to fetch all bills
        ResponseEntity<List> response = restTemplate.exchange(url, HttpMethod.GET, null, List.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            model.addAttribute("bills", response.getBody()); // Add list of bills to the model
        } else {
            model.addAttribute("errorMessage", "Error occurred while fetching bills.");
        }

        return "billView"; // This will render billView.html
    }

   

    @GetMapping("/searchBill")
    public String searchBill(@RequestParam("billId") Long billId, Model model) {
        String url = BASE_URL + "/api/bills/viewBill/" + billId; // API call to fetch bill by ID
        ResponseEntity<Bill> response = restTemplate.exchange(url, HttpMethod.GET, null, Bill.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            Bill bill = response.getBody(); // Get the bill object from the response
            model.addAttribute("bill", bill);

            // Log the isInsuranceApplicable value
          //  System.out.println("Received isInsuranceApplicable: " + bill.getIsInsuranceApplicable()); // Log the value

            model.addAttribute("successMessage", "Bill found with ID: " + billId);
        } else {
            model.addAttribute("errorMessage", "Bill not found with ID: " + billId);
            model.addAttribute("bills", List.of()); // Clear any existing bills in case of no results
        }

        return "billView"; // Only update the table container dynamically
    }
   
    @GetMapping("/fetchConsultationFee")
    public String fetchConsultationFee(@RequestParam("appointmentId") int appointmentId, Model model) {
        String url = BASE_URL + "/api/bills/consultationFees/" + appointmentId;

        ResponseEntity<Double> response = restTemplate.exchange(
            url, 
            HttpMethod.GET, 
            null, 
            Double.class
        );

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            Bill bill = new Bill();
            bill.setConsultationFees(response.getBody()); // Set the fetched consultation fee
            
            model.addAttribute("bill", bill);  // Ensure the Bill object is in the model
            model.addAttribute("appointmentId", appointmentId);
        } else {
            model.addAttribute("errorMessage", "Consultation fee not found for Appointment ID: " + appointmentId);
        }

        return "billAdd";  // Return to the form view
    }

}