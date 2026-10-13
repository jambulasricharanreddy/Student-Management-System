package com.smartstudenthub.controller;

import com.smartstudenthub.dto.AssignmentRequest;
import com.smartstudenthub.entity.Assignment;
import com.smartstudenthub.entity.AssignmentSubmission;
import com.smartstudenthub.service.AssignmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/assignments")
@Tag(name = "Assignment Management", description = "Course assignments, submissions, and grading")
public class AssignmentController {

    private final AssignmentService assignmentService;

    public AssignmentController(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    @PostMapping(value = "/{lecturerId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('LECTURER', 'ADMIN')")
    @Operation(summary = "Create assignment with optional briefing attachment")
    public ResponseEntity<Assignment> createAssignment(
            @PathVariable Long lecturerId,
            @RequestParam String title,
            @RequestParam(required = false) String description,
            @RequestParam Long courseId,
            @RequestParam Long subjectId,
            @RequestParam String dueDate,
            @RequestParam(defaultValue = "100.00") BigDecimal maxMarks,
            @RequestPart(value = "attachment", required = false) MultipartFile attachment) {

        AssignmentRequest request = new AssignmentRequest();
        request.setTitle(title);
        request.setDescription(description);
        request.setCourseId(courseId);
        request.setSubjectId(subjectId);
        request.setDueDate(LocalDateTime.parse(dueDate));
        request.setMaxMarks(maxMarks);

        return ResponseEntity.ok(assignmentService.createAssignment(lecturerId, request, attachment));
    }

    @GetMapping("/course/{courseId}")
    @Operation(summary = "Get assignments for course")
    public ResponseEntity<List<Assignment>> getAssignmentsByCourse(@PathVariable Long courseId) {
        return ResponseEntity.ok(assignmentService.getAssignmentsByCourse(courseId));
    }

    @GetMapping("/lecturer/{lecturerId}")
    @PreAuthorize("hasAnyRole('LECTURER', 'ADMIN')")
    @Operation(summary = "Get assignments posted by lecturer")
    public ResponseEntity<List<Assignment>> getAssignmentsByLecturer(@PathVariable Long lecturerId) {
        return ResponseEntity.ok(assignmentService.getAssignmentsByLecturer(lecturerId));
    }

    @PostMapping(value = "/{assignmentId}/submit/{studentId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('STUDENT')")
    @Operation(summary = "Submit student assignment solution file")
    public ResponseEntity<AssignmentSubmission> submitAssignment(
            @PathVariable Long assignmentId,
            @PathVariable Long studentId,
            @RequestPart("file") MultipartFile file) {
        return ResponseEntity.ok(assignmentService.submitAssignment(studentId, assignmentId, file));
    }

    @GetMapping("/{assignmentId}/submissions")
    @PreAuthorize("hasAnyRole('LECTURER', 'ADMIN')")
    @Operation(summary = "Get all student submissions for an assignment")
    public ResponseEntity<List<AssignmentSubmission>> getSubmissions(@PathVariable Long assignmentId) {
        return ResponseEntity.ok(assignmentService.getSubmissionsForAssignment(assignmentId));
    }

    @GetMapping("/student/{studentId}/submissions")
    @Operation(summary = "Get student submission history")
    public ResponseEntity<List<AssignmentSubmission>> getStudentSubmissions(@PathVariable Long studentId) {
        return ResponseEntity.ok(assignmentService.getStudentSubmissions(studentId));
    }

    @PostMapping("/submissions/{submissionId}/grade")
    @PreAuthorize("hasAnyRole('LECTURER', 'ADMIN')")
    @Operation(summary = "Grade student assignment submission")
    public ResponseEntity<AssignmentSubmission> gradeSubmission(
            @PathVariable Long submissionId,
            @RequestBody Map<String, Object> body) {
        BigDecimal marks = new BigDecimal(body.get("marks").toString());
        String feedback = body.get("feedback") != null ? body.get("feedback").toString() : "";
        return ResponseEntity.ok(assignmentService.gradeSubmission(submissionId, marks, feedback));
    }
}
