package com.smartstudenthub.controller;

import com.smartstudenthub.dto.FeeAssignRequest;
import com.smartstudenthub.dto.FeeCategoryRequest;
import com.smartstudenthub.dto.FeePaymentRequest;
import com.smartstudenthub.entity.FeeCategory;
import com.smartstudenthub.entity.FeePayment;
import com.smartstudenthub.entity.StudentFee;
import com.smartstudenthub.service.AuthService;
import com.smartstudenthub.service.FeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fees")
@Tag(name = "Fee Management", description = "Fee Categories, Allocations, Payments, and Receipts")
public class FeeController {

    private final FeeService feeService;
    private final AuthService authService;

    public FeeController(FeeService feeService, AuthService authService) {
        this.feeService = feeService;
        this.authService = authService;
    }

    @GetMapping("/categories")
    @Operation(summary = "Get all fee categories")
    public ResponseEntity<List<FeeCategory>> getCategories() {
        return ResponseEntity.ok(feeService.getAllCategories());
    }

    @PostMapping("/categories")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create fee category (Admin only)")
    public ResponseEntity<FeeCategory> createCategory(@Valid @RequestBody FeeCategoryRequest request) {
        return ResponseEntity.ok(feeService.createCategory(request));
    }

    @PostMapping("/assign")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Assign fee to student (Admin only)")
    public ResponseEntity<StudentFee> assignFee(@Valid @RequestBody FeeAssignRequest request) {
        return ResponseEntity.ok(feeService.assignFee(request));
    }

    @PostMapping("/pay")
    @PreAuthorize("hasAnyRole('STUDENT', 'ADMIN')")
    @Operation(summary = "Record fee payment with simulated receipt")
    public ResponseEntity<FeePayment> recordPayment(@Valid @RequestBody FeePaymentRequest request) {
        String recorder = authService.getCurrentUser().getFullName();
        return ResponseEntity.ok(feeService.recordPayment(request, recorder));
    }

    @GetMapping("/student/{studentId}")
    @Operation(summary = "Get fee breakdown for a student")
    public ResponseEntity<List<StudentFee>> getStudentFees(@PathVariable Long studentId) {
        return ResponseEntity.ok(feeService.getStudentFees(studentId));
    }

    @GetMapping("/student/{studentId}/payments")
    @Operation(summary = "Get payment history for a student")
    public ResponseEntity<List<FeePayment>> getStudentPayments(@PathVariable Long studentId) {
        return ResponseEntity.ok(feeService.getPaymentsByStudent(studentId));
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all student fee assignments (Admin only)")
    public ResponseEntity<List<StudentFee>> getAllStudentFees() {
        return ResponseEntity.ok(feeService.getAllStudentFees());
    }

    @GetMapping("/receipt/{receiptNumber}")
    @Operation(summary = "Get payment receipt by receipt number")
    public ResponseEntity<FeePayment> getReceipt(@PathVariable String receiptNumber) {
        return ResponseEntity.ok(feeService.getPaymentByReceiptNumber(receiptNumber));
    }
}
