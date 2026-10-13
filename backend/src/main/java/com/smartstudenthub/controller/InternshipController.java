package com.smartstudenthub.controller;

import com.smartstudenthub.dto.InternshipRequest;
import com.smartstudenthub.entity.Internship;
import com.smartstudenthub.entity.InternshipApplication;
import com.smartstudenthub.entity.ResumeProfile;
import com.smartstudenthub.entity.User;
import com.smartstudenthub.service.AuthService;
import com.smartstudenthub.service.InternshipService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/internships")
@Tag(name = "Internship Portal & Resume Builder", description = "Listings, Applications, and Resume Profiles")
public class InternshipController {

    private final InternshipService internshipService;
    private final AuthService authService;

    public InternshipController(InternshipService internshipService, AuthService authService) {
        this.internshipService = internshipService;
        this.authService = authService;
    }

    @GetMapping
    @Operation(summary = "Get all open internships")
    public ResponseEntity<List<Internship>> getOpenInternships() {
        return ResponseEntity.ok(internshipService.getAllOpenInternships());
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN', 'LECTURER')")
    @Operation(summary = "Get all internships including closed")
    public ResponseEntity<List<Internship>> getAllInternships() {
        return ResponseEntity.ok(internshipService.getAllInternships());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get internship details")
    public ResponseEntity<Internship> getInternshipById(@PathVariable Long id) {
        return ResponseEntity.ok(internshipService.getInternshipById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'LECTURER')")
    @Operation(summary = "Post new internship listing")
    public ResponseEntity<Internship> createInternship(@Valid @RequestBody InternshipRequest request) {
        User user = authService.getCurrentUser();
        return ResponseEntity.ok(internshipService.createInternship(request, user));
    }

    @PostMapping("/{id}/apply/{studentId}")
    @PreAuthorize("hasRole('STUDENT')")
    @Operation(summary = "Submit internal application for internship")
    public ResponseEntity<InternshipApplication> apply(
            @PathVariable Long id,
            @PathVariable Long studentId,
            @RequestBody Map<String, String> body) {
        String coverLetter = body.get("coverLetter");
        String resumePath = body.get("resumePath");
        return ResponseEntity.ok(internshipService.apply(studentId, id, coverLetter, resumePath));
    }

    @GetMapping("/student/{studentId}/applications")
    @Operation(summary = "Get student's internship applications")
    public ResponseEntity<List<InternshipApplication>> getStudentApplications(@PathVariable Long studentId) {
        return ResponseEntity.ok(internshipService.getStudentApplications(studentId));
    }

    @GetMapping("/{id}/applications")
    @PreAuthorize("hasAnyRole('ADMIN', 'LECTURER')")
    @Operation(summary = "Get all applicants for an internship")
    public ResponseEntity<List<InternshipApplication>> getInternshipApplications(@PathVariable Long id) {
        return ResponseEntity.ok(internshipService.getInternshipApplications(id));
    }

    @PatchMapping("/applications/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'LECTURER')")
    @Operation(summary = "Update internship application status")
    public ResponseEntity<InternshipApplication> updateApplicationStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(internshipService.updateApplicationStatus(id, body.get("status"), body.get("reviewNotes")));
    }

    // Resume Profile
    @GetMapping("/resume-profile/{studentId}")
    @Operation(summary = "Get student resume builder profile data")
    public ResponseEntity<ResumeProfile> getResumeProfile(@PathVariable Long studentId) {
        return ResponseEntity.ok(internshipService.getResumeProfile(studentId));
    }

    @PutMapping("/resume-profile/{studentId}")
    @PreAuthorize("hasRole('STUDENT')")
    @Operation(summary = "Update student resume builder data")
    public ResponseEntity<ResumeProfile> updateResumeProfile(
            @PathVariable Long studentId,
            @RequestBody ResumeProfile updated) {
        return ResponseEntity.ok(internshipService.saveResumeProfile(studentId, updated));
    }
}
