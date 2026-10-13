package com.smartstudenthub.service;

import com.smartstudenthub.dto.FeeAssignRequest;
import com.smartstudenthub.dto.FeeCategoryRequest;
import com.smartstudenthub.dto.FeePaymentRequest;
import com.smartstudenthub.entity.*;
import com.smartstudenthub.exception.BadRequestException;
import com.smartstudenthub.exception.ConflictException;
import com.smartstudenthub.exception.ResourceNotFoundException;
import com.smartstudenthub.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class FeeService {

    private final FeeCategoryRepository feeCategoryRepository;
    private final StudentFeeRepository studentFeeRepository;
    private final FeePaymentRepository paymentRepository;
    private final StudentRepository studentRepository;

    public FeeService(
            FeeCategoryRepository feeCategoryRepository,
            StudentFeeRepository studentFeeRepository,
            FeePaymentRepository paymentRepository,
            StudentRepository studentRepository) {
        this.feeCategoryRepository = feeCategoryRepository;
        this.studentFeeRepository = studentFeeRepository;
        this.paymentRepository = paymentRepository;
        this.studentRepository = studentRepository;
    }

    public List<FeeCategory> getAllCategories() { return feeCategoryRepository.findAll(); }

    @Transactional
    public FeeCategory createCategory(FeeCategoryRequest request) {
        if (feeCategoryRepository.existsByName(request.getName())) {
            throw new ConflictException("Fee category already exists: " + request.getName());
        }
        FeeCategory cat = new FeeCategory(request.getName(), request.getDescription(), request.getDefaultAmount());
        return feeCategoryRepository.save(cat);
    }

    @Transactional
    public StudentFee assignFee(FeeAssignRequest request) {
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
        FeeCategory cat = feeCategoryRepository.findById(request.getFeeCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Fee category not found"));

        BigDecimal amount = request.getAmountDue() != null ? request.getAmountDue() : cat.getDefaultAmount();

        StudentFee studentFee = new StudentFee(
                student,
                cat,
                request.getAcademicYear(),
                request.getSemester(),
                amount,
                request.getDueDate()
        );
        return studentFeeRepository.save(studentFee);
    }

    @Transactional
    public synchronized FeePayment recordPayment(FeePaymentRequest request, String recordedBy) {
        StudentFee studentFee = studentFeeRepository.findById(request.getStudentFeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Fee record not found"));

        if (request.getAmount() == null || request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Payment amount must be greater than zero");
        }

        if (request.getAmount().compareTo(studentFee.getBalance()) > 0) {
            throw new BadRequestException("Payment amount (" + request.getAmount() + ") exceeds remaining balance (" + studentFee.getBalance() + ")");
        }

        studentFee.setAmountPaid(studentFee.getAmountPaid().add(request.getAmount()));
        studentFee.recalculateBalance();
        studentFeeRepository.save(studentFee);

        String receiptNum = "REC-" + LocalDateTime.now().getYear() + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        String txRef = request.getTransactionReference() != null && !request.getTransactionReference().trim().isEmpty()
                ? request.getTransactionReference() : "TXN-" + UUID.randomUUID().toString().substring(0, 10).toUpperCase();

        FeePayment payment = new FeePayment();
        payment.setStudentFee(studentFee);
        payment.setStudent(studentFee.getStudent());
        payment.setAmount(request.getAmount());
        payment.setPaymentDate(LocalDateTime.now());
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setTransactionReference(txRef);
        payment.setReceiptNumber(receiptNum);
        payment.setStatus("SUCCESS");
        payment.setRecordedBy(recordedBy);

        return paymentRepository.save(payment);
    }

    public List<StudentFee> getStudentFees(Long studentId) {
        return studentFeeRepository.findByStudentId(studentId);
    }

    public List<StudentFee> getAllStudentFees() {
        return studentFeeRepository.findAll();
    }

    public List<FeePayment> getPaymentsByStudent(Long studentId) {
        return paymentRepository.findByStudentId(studentId);
    }

    public FeePayment getPaymentByReceiptNumber(String receiptNumber) {
        return paymentRepository.findByReceiptNumber(receiptNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Receipt not found: " + receiptNumber));
    }
}
