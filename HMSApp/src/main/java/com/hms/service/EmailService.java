package com.hms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    // Method to send email notification
    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("saran94422@gmail.com"); // Replace with your email or use environment variables
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
        System.out.println("Mail sent successfully to " + to);
    }

    // New method to notify when a patient is added
    public void notifyPatientAdded(String patientEmail, String patientName) {
        String subject = "Patient Added: " + patientName;
        String text = "Dear Team,\n\nA new patient has been added.\n\nPatient Name: " + patientName +
                      "\n\nPlease review the patient details in the system.";
        sendEmail(patientEmail, subject, text);
    }

    // New method to notify when a patient is updated
    public void notifyPatientUpdated(String patientEmail, String patientName) {
        String subject = "Patient Updated: " + patientName;
        String text = "Dear Team,\n\nThe details of the patient have been updated.\n\nPatient Name: " + patientName +
                      "\n\nPlease review the updated patient information in the system.";
        sendEmail(patientEmail, subject, text);
    }
}
