package com.smartstudenthub.dto;

import jakarta.validation.constraints.NotBlank;

public class AttendanceCheckInRequest {

    @NotBlank(message = "Session token is required")
    private String sessionToken;

    public AttendanceCheckInRequest() {}

    public AttendanceCheckInRequest(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public String getSessionToken() { return sessionToken; }
    public void setSessionToken(String sessionToken) { this.sessionToken = sessionToken; }
}
