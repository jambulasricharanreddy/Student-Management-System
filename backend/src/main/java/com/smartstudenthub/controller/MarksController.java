package com.smartstudenthub.controller;

import com.smartstudenthub.dto.AssessmentRequest;
import com.smartstudenthub.dto.MarkEntryDto;
import com.smartstudenthub.entity.Assessment;
import com.smartstudenthub.entity.Mark;
import com.smartstudenthub.service.AuthService;
import com.smartstudenthub.service.MarksService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/marks")
@Tag(name = "Marks & Performance Analytics", description = "Assessments, Grading, Transcript Reports")
public class MarksController {

    private final MarksService marksService;
    private final AuthService authService;

    public MarksController(MarksService marksService, AuthService authService) {
        this.marksService = marksService;
        this.authService = authService;
    }

    @PostMapping("/assessments/{lecturerId}")
    @PreAuthorize("hasAnyRole('LECTURER', 'ADMIN')")
    @Operation(summary = "Create assessment (Exam/Quiz/Internal)")
    public ResponseEntity<Assessment> createAssessment(
            @PathVariable Long lecturerId,
            @Valid @RequestBody AssessmentRequest request) {
        return ResponseEntity.ok(marksService.createAssessment(lecturerId, request));
    }

    @GetMapping("/assessments/course/{courseId}")
    @Operation(summary = "Get all assessments for a course")
    public ResponseEntity<List<Assessment>> getAssessmentsByCourse(@PathVariable Long courseId) {
        return ResponseEntity.ok(marksService.getAssessmentsByCourse(courseId));
    }

    @PostMapping("/assessments/{assessmentId}/entries")
    @PreAuthorize("hasAnyRole('LECTURER', 'ADMIN')")
    @Operation(summary = "Record or update student marks for an assessment")
    public ResponseEntity<List<Mark>> saveMarks(
            @PathVariable Long assessmentId,
            @Valid @RequestBody List<MarkEntryDto> entries) {
        String username = authService.getCurrentUser().getFullName();
        return ResponseEntity.ok(marksService.saveMarks(assessmentId, entries, username));
    }

    @PatchMapping("/assessments/{assessmentId}/publish")
    @PreAuthorize("hasAnyRole('LECTURER', 'ADMIN')")
    @Operation(summary = "Publish assessment results to students")
    public ResponseEntity<Assessment> publishAssessment(
            @PathVariable Long assessmentId,
            @RequestParam boolean published) {
        return ResponseEntity.ok(marksService.setPublishedStatus(assessmentId, published));
    }

    @GetMapping("/assessments/{assessmentId}/entries")
    @PreAuthorize("hasAnyRole('LECTURER', 'ADMIN')")
    @Operation(summary = "Get all marks recorded for an assessment")
    public ResponseEntity<List<Mark>> getAssessmentMarks(@PathVariable Long assessmentId) {
        return ResponseEntity.ok(marksService.getMarksForAssessment(assessmentId));
    }

    @GetMapping("/student/{studentId}/report")
    @Operation(summary = "Get comprehensive student transcript report and academic warning flags")
    public ResponseEntity<Map<String, Object>> getStudentReport(@PathVariable Long studentId) {
        return ResponseEntity.ok(marksService.getStudentPerformanceReport(studentId));
    }
}
