package com.hms.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;


import java.time.LocalDate;
import java.util.List;



@Entity
public class Bill {
	 @Id
	
	    @GeneratedValue(strategy = GenerationType.IDENTITY) 
	 @Column(name = "bill_id")
	 private Long billId;
	
	 private double consultationFees;
	 private double medicineFees;
	 private double testCharge;
	   
     public Boolean getIsInsuranceApplicable() {
		return isInsuranceApplicable;
	}

	public void setIsInsuranceApplicable(Boolean isInsuranceApplicable) {
		this.isInsuranceApplicable = isInsuranceApplicable;
	}

	public LocalDate getBillDate() {
		return billDate;
	}

	public void setBillDate(LocalDate billDate) {
		this.billDate = billDate;
	}

	public double getTestCharge() {
		return testCharge;
	}

	private double  miscellaneousCharge;
     public Bill(Long billId, double consultationFees, double medicineFees, double tescharge, double miscellaneouscharge,
			String description, Boolean isInsuranceApplicable, double insuranceClaimedAmount, float discountPercentage,
			double taxes, LocalDate billDate, Appointment appointment, List<Payment> pmtList) {
		super();
		this.billId = billId;
		this.consultationFees = consultationFees;
		this.medicineFees = medicineFees;
		this.testCharge = tescharge;
		this.miscellaneousCharge = miscellaneouscharge;
		this.description = description;
		this.isInsuranceApplicable = isInsuranceApplicable;
		this.insuranceClaimedAmount = insuranceClaimedAmount;
		this.discountPercentage = discountPercentage;
		this.taxes = taxes;
		billDate = billDate;
		this.appointment = appointment;
		this.pmtList = pmtList;
	}

	private  String description;
	@Column(name = "is_insurance_applicable")
    private Boolean isInsuranceApplicable;
   
    private double insuranceClaimedAmount;
    private float  discountPercentage;
    private double taxes;
    
    private LocalDate billDate;
   
   
    @OneToOne
    @JoinColumn(name = "appointment_id", nullable = false)
    @JsonIgnoreProperties("billObj")
    private Appointment appointment;

    @OneToMany(mappedBy = "billObj", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Payment> pmtList;
    
    public Bill() {}

	public Long getBillId() {
		return billId;
	}

	public void setBillId(Long billId) {
		this.billId = billId;
	}

	public double getConsultationFees() {
		return consultationFees;
	}

	public void setConsultationFees(double consultationFees) {
		this.consultationFees = consultationFees;
	}

	public double getMedicineFees() {
		return medicineFees;
	}

	public void setMedicineFees(double medicineFees) {
		this.medicineFees = medicineFees;
	}

	
	public void setTestCharge(double testCharge) {
		this.testCharge = testCharge;
	}

	public double getMiscellaneousCharge() {
		return miscellaneousCharge;
	}

	public void setMiscellaneousCharge(double miscellaneouscharge) {
		this.miscellaneousCharge = miscellaneouscharge;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	
	

	public double getInsuranceClaimedAmount() {
		return insuranceClaimedAmount;
	}

	public void setInsuranceClaimedAmount(double insuranceClaimedAmount) {
		this.insuranceClaimedAmount = insuranceClaimedAmount;
	}

	public float getDiscountPercentage() {
		return discountPercentage;
	}

	public void setDiscountPercentage(float discountPercentage) {
		this.discountPercentage = discountPercentage;
	}

	public double getTaxes() {
		return taxes;
	}

	public void setTaxes(double taxes) {
		this.taxes = taxes;
	}

	

	public Appointment getAppointment() {
		return appointment;
	}

	public void setAppointment(Appointment appointment) {
		this.appointment = appointment;
	}

	public List<Payment> getPmtList() {
		return pmtList;
	}

	public void setPmtList(List<Payment> pmtList) {
		this.pmtList = pmtList;
	}
   
}
    
    