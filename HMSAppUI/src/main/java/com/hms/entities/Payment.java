package com.hms.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long paymentId;

    @ManyToOne
    @JoinColumn(name = "bill_id", nullable = false)
    @NotNull(message = "Bill reference cannot be null")
    @JsonIgnore
    private Bill billObj;

    @ManyToOne
    @JoinColumn(name = "appointment_id") 
    @JsonIgnore// Adding the appointment reference here
    private Appointment appointment;

    @NotNull(message = "Transaction ID cannot be null")
    @Size(min = 10, max = 50, message = "Transaction ID must be between 10 and 50 characters")
    private String transactionId;

    @NotNull(message = "Payment date cannot be null")
    @Temporal(TemporalType.DATE)
    private LocalDate paymentDate;

    @Positive(message = "Amount paid must be greater than zero")
    private double amountPaid;

    @NotNull(message = "Payment method cannot be null")
    @Size(min = 3, max = 50, message = "Payment method must be between 3 and 50 characters")
    private String paymentMethod;

    @NotNull(message = "Payment status cannot be null")
    @Size(min = 5, max = 20, message = "Payment status must be between 5 and 20 characters")
    @Column(name = "payment_status")
    private String paymentStatus;

    // Default constructor required by JPA
    public Payment() {}

    // Getters and Setters
    public long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(long paymentId) {
        this.paymentId = paymentId;
    }

    public Bill getBillObj() {
        return billObj;
    }

    public void setBillObj(Bill billObj) {
        this.billObj = billObj;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public double getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(double amountPaid) {
        this.amountPaid = amountPaid;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}
