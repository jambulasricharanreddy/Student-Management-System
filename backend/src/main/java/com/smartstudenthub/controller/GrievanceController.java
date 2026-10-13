package com.smartstudenthub.controller;

import com.smartstudenthub.dto.GrievanceRequest;
import com.smartstudenthub.dto.GrievanceStatusUpdateRequest;
import com.smartstudenthub.entity.Grievance;
import com.smartstudenthub.entity.GrievanceComment;
import com.smartstudenthub.entity.RoleType;
import com.smartstudenthub.entity.User;
import com.smartstudenthub.service.AuthService;
import com.smartstudenthub.service.GrievanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/grievances")
@Tag(name = "Grievance & Support Portal", description = "Support tickets, status tracking, and staff comments")
public class GrievanceController {

    private final GrievanceService grievanceService;
    private final AuthService authService;

    public GrievanceController(GrievanceService grievanceService, AuthService authService) {
        this.grievanceService = grievanceService;
        this.authService = authService;
    }

    @PostMapping("/{studentId}")
    @PreAuthorize("hasRole('STUDENT')")
    @Operation(summary = "Submit a grievance support ticket")
    public ResponseEntity<Grievance> submitGrievance(
            @PathVariable Long studentId,
            @Valid @RequestBody GrievanceRequest request) {
        return ResponseEntity.ok(grievanceService.submitGrievance(studentId, request));
    }

    @GetMapping("/student/{studentId}")
    @Operation(summary = "Get student's submitted grievances")
    public ResponseEntity<Page<Grievance>> getStudentGrievances(
            @PathVariable Long studentId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ResponseEntity.ok(grievanceService.getStudentGrievances(studentId, pageRequest));
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN', 'LECTURER')")
    @Operation(summary = "Get all campus grievances (Admin/Staff only)")
    public ResponseEntity<List<Grievance>> getAllGrievances() {
        return ResponseEntity.ok(grievanceService.getAllGrievances());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get grievance details by ID")
    public ResponseEntity<Grievance> getGrievanceById(@PathVariable Long id) {
        return ResponseEntity.ok(grievanceService.getGrievanceById(id));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'LECTURER')")
    @Operation(summary = "Update grievance status and assign staff")
    public ResponseEntity<Grievance> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody GrievanceStatusUpdateRequest request) {
        User currentUser = authService.getCurrentUser();
        return ResponseEntity.ok(grievanceService.updateStatus(id, request, currentUser));
    }

    @PostMapping("/{id}/comments")
    @Operation(summary = "Post comment to grievance thread")
    public ResponseEntity<GrievanceComment> addComment(
            @PathVariable Long id,
            @RequestBody Map<String, Object> body) {
        User user = authService.getCurrentUser();
        String text = body.get("comment").toString();
        boolean isInternal = body.get("isInternal") != null && Boolean.parseBoolean(body.get("isInternal").toString());
        return ResponseEntity.ok(grievanceService.addComment(id, user, text, isInternal));
    }

    @GetMapping("/{id}/comments")
    @Operation(summary = "Get comments thread for a grievance")
    public ResponseEntity<List<GrievanceComment>> getComments(@PathVariable Long id) {
        User user = authService.getCurrentUser();
        boolean isStaff = user.getRole().getName() != RoleType.ROLE_STUDENT;
        return ResponseEntity.ok(grievanceService.getComments(id, isStaff));
    }
}
