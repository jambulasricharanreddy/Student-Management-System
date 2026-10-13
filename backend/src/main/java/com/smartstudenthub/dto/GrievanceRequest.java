package com.smartstudenthub.dto;

import jakarta.validation.constraints.NotBlank;

public class GrievanceRequest {

    @NotBlank(message = "Category is required")
    private String category; // ACADEMIC, HOSTEL, FEE, INFRASTRUCTURE, OTHER

    @NotBlank(message = "Subject is required")
    private String subject;

    @NotBlank(message = "Description is required")
    private String description;

    private String priority = "MEDIUM"; // LOW, MEDIUM, HIGH, URGENT

    public GrievanceRequest() {}

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
}
