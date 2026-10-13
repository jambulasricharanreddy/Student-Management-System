package com.smartstudenthub.controller;

import com.smartstudenthub.dto.CertificateRequest;
import com.smartstudenthub.entity.Certificate;
import com.smartstudenthub.service.CertificateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/certificates")
@Tag(name = "Digital Certificates", description = "Certificate Issuance, Public Verification, and Revocation")
public class CertificateController {

    private final CertificateService certificateService;

    public CertificateController(CertificateService certificateService) {
        this.certificateService = certificateService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'LECTURER')")
    @Operation(summary = "Issue a verified digital certificate")
    public ResponseEntity<Certificate> issueCertificate(@Valid @RequestBody CertificateRequest request) {
        return ResponseEntity.ok(certificateService.issueCertificate(request));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'LECTURER')")
    @Operation(summary = "Get all certificates")
    public ResponseEntity<List<Certificate>> getAllCertificates() {
        return ResponseEntity.ok(certificateService.getAllCertificates());
    }

    @GetMapping("/student/{studentId}")
    @Operation(summary = "Get all certificates awarded to a student")
    public ResponseEntity<List<Certificate>> getStudentCertificates(@PathVariable Long studentId) {
        return ResponseEntity.ok(certificateService.getStudentCertificates(studentId));
    }

    @GetMapping("/verify/{verificationId}")
    @Operation(summary = "Public certificate verification endpoint (minimal privacy-preserving data)")
    public ResponseEntity<Map<String, Object>> verifyCertificate(@PathVariable String verificationId) {
        return ResponseEntity.ok(certificateService.verifyPublicCertificate(verificationId));
    }

    @PatchMapping("/{id}/revoke")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Revoke digital certificate with reason (Admin only)")
    public ResponseEntity<Certificate> revokeCertificate(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(certificateService.revokeCertificate(id, body.get("reason")));
    }
}
