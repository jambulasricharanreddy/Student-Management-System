package com.smartstudenthub.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class CertificateRequest {

    @NotNull(message = "Student ID is required")
    private Long studentId;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Certificate type is required")
    private String certificateType; // COURSE_COMPLETION, MERIT, WORKSHOP, EXCELLENCE

    @NotNull(message = "Issue date is required")
    private LocalDate issueDate;

    @NotBlank(message = "Issuer name is required")
    private String issuerName;

    private String description;

    public CertificateRequest() {}

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getCertificateType() { return certificateType; }
    public void setCertificateType(String certificateType) { this.certificateType = certificateType; }

    public LocalDate getIssueDate() { return issueDate; }
    public void setIssueDate(LocalDate issueDate) { this.issueDate = issueDate; }

    public String getIssuerName() { return issuerName; }
    public void setIssuerName(String issuerName) { this.issuerName = issuerName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
