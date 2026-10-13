package com.smartstudenthub.service;

import com.smartstudenthub.dto.CertificateRequest;
import com.smartstudenthub.entity.Certificate;
import com.smartstudenthub.entity.Student;
import com.smartstudenthub.exception.BadRequestException;
import com.smartstudenthub.exception.ResourceNotFoundException;
import com.smartstudenthub.repository.CertificateRepository;
import com.smartstudenthub.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class CertificateService {

    private final CertificateRepository certificateRepository;
    private final StudentRepository studentRepository;

    public CertificateService(CertificateRepository certificateRepository, StudentRepository studentRepository) {
        this.certificateRepository = certificateRepository;
        this.studentRepository = studentRepository;
    }

    @Transactional
    public Certificate issueCertificate(CertificateRequest request) {
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        Certificate cert = new Certificate();
        cert.setVerificationId("SSH-CERT-" + UUID.randomUUID().toString().toUpperCase());
        cert.setStudent(student);
        cert.setTitle(request.getTitle());
        cert.setCertificateType(request.getCertificateType());
        cert.setIssueDate(request.getIssueDate());
        cert.setIssuerName(request.getIssuerName());
        cert.setDescription(request.getDescription());
        cert.setRevoked(false);

        return certificateRepository.save(cert);
    }

    public List<Certificate> getStudentCertificates(Long studentId) {
        return certificateRepository.findByStudentId(studentId);
    }

    public List<Certificate> getAllCertificates() {
        return certificateRepository.findAll();
    }

    public Certificate getCertificateById(Long id) {
        return certificateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Certificate not found"));
    }

    /**
     * Public verification endpoint exposing ONLY minimal non-sensitive information.
     */
    public Map<String, Object> verifyPublicCertificate(String verificationId) {
        Certificate cert = certificateRepository.findByVerificationId(verificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Certificate verification ID not recognized"));

        Map<String, Object> publicInfo = new HashMap<>();
        publicInfo.put("verificationId", cert.getVerificationId());
        publicInfo.put("studentName", cert.getStudent().getUser().getFullName());
        publicInfo.put("courseName", cert.getStudent().getCourse().getName());
        publicInfo.put("certificateTitle", cert.getTitle());
        publicInfo.put("certificateType", cert.getCertificateType());
        publicInfo.put("issueDate", cert.getIssueDate());
        publicInfo.put("issuerName", cert.getIssuerName());
        publicInfo.put("status", cert.isRevoked() ? "REVOKED" : "VALID");
        if (cert.isRevoked()) {
            publicInfo.put("revocationReason", cert.getRevocationReason());
        }
        return publicInfo;
    }

    @Transactional
    public Certificate revokeCertificate(Long id, String reason) {
        Certificate cert = getCertificateById(id);
        cert.setRevoked(true);
        cert.setRevocationReason(reason);
        return certificateRepository.save(cert);
    }
}
