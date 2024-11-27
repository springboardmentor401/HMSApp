package com.hms.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import java.time.LocalDate;

@Entity
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int billId;

    private double medicines;
    private double testcharges;
    private double miscellaneous;
    private String description;
    private boolean isInsuranceApplicable;
    private float discountPercentage;
    private boolean tax;
    private LocalDate billDate;

    // Getters and Setters
    public int getBillId() {
        return billId;
    }

    public void setBillId(int billId) {
        this.billId = billId;
    }

    public double getMedicines() {
        return medicines;
    }

    public void setMedicines(double medicines) {
        this.medicines = medicines;
    }

    public double getTestcharges() {
        return testcharges;
    }

    public void setTestcharges(double testcharges) {
        this.testcharges = testcharges;
    }

    public double getMiscellaneous() {
        return miscellaneous;
    }

    public void setMiscellaneous(double miscellaneous) {
        this.miscellaneous = miscellaneous;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isInsuranceApplicable() {
        return isInsuranceApplicable;
    }

    public void setInsuranceApplicable(boolean insuranceApplicable) {
        isInsuranceApplicable = insuranceApplicable;
    }

    public float getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(float discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public boolean isTax() {
        return tax;
    }

    public void setTax(boolean tax) {
        this.tax = tax;
    }

    public LocalDate getBillDate() {
        return billDate;
    }

    public void setBillDate(LocalDate billDate) {
        this.billDate = billDate;
    }
}
