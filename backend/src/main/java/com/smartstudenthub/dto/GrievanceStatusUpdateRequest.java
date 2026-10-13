package com.smartstudenthub.dto;

import jakarta.validation.constraints.NotBlank;

public class GrievanceStatusUpdateRequest {

    @NotBlank(message = "Status is required")
    private String status; // SUBMITTED, UNDER_REVIEW, IN_PROGRESS, RESOLVED, REJECTED

    private Long assignedToUserId;
    private String resolutionNotes;
    private String comment;
    private boolean internalComment = false;

    public GrievanceStatusUpdateRequest() {}

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Long getAssignedToUserId() { return assignedToUserId; }
    public void setAssignedToUserId(Long assignedToUserId) { this.assignedToUserId = assignedToUserId; }

    public String getResolutionNotes() { return resolutionNotes; }
    public void setResolutionNotes(String resolutionNotes) { this.resolutionNotes = resolutionNotes; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public boolean isInternalComment() { return internalComment; }
    public void setInternalComment(boolean internalComment) { this.internalComment = internalComment; }
}
