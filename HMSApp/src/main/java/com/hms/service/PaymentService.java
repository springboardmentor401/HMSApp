package com.hms.service;

import org.springframework.stereotype.Service;

import com.hms.entities.Bill;
import com.hms.entities.Payment;
import com.hms.repository.BillRepository;
import com.hms.repository.PaymentRepository;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final BillRepository billRepo;

    public PaymentRepository getPaymentRepository() {
        return paymentRepository;
    }

    public BillRepository getBillRepo() {
        return billRepo;
    }

    public PaymentService(PaymentRepository paymentRepository, BillRepository billRepo) {
        this.paymentRepository = paymentRepository;
        this.billRepo = billRepo;
    }

    // Add a new payment
    public Payment addPayment(Payment payment, long billId) {
        Optional<Bill> op = billRepo.findById(billId);

        if (op.isPresent()) {
            Bill b = op.get();
            payment.setBillObj(b);
            paymentRepository.save(payment);
            return payment;
        } else {
            return null; // Or throw an exception if preferred
        }
    }

    // View payment by ID
    public Payment getPaymentById(Integer id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found with ID: " + id));
    }

    // View all payments
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    // Update payment
    public Payment updatePayment(Integer id, Payment updatedPayment) {
        return paymentRepository.findById(id).map(existingPayment -> {
            // Update necessary fields
            existingPayment.setAmountPaid(updatedPayment.getAmountPaid());
            existingPayment.setPaymentDate(updatedPayment.getPaymentDate());
            existingPayment.setPaymentMethod(updatedPayment.getPaymentMethod());
            existingPayment.setBillObj(updatedPayment.getBillObj());
            return paymentRepository.save(existingPayment);
        }).orElseThrow(() -> new RuntimeException("Payment not found with ID: " + id));
    }

    // Delete payment by ID
    public void deletePayment(Integer id) {
        if (paymentRepository.existsById(id)) {
            paymentRepository.deleteById(id);
        } else {
            throw new RuntimeException("Payment not found with ID: " + id);
        }
    }
}
