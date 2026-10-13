package com.smartstudenthub.controller;

import com.smartstudenthub.dto.TimetableRequest;
import com.smartstudenthub.entity.TimetableEntry;
import com.smartstudenthub.service.TimetableService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/timetable")
@Tag(name = "Timetable Management", description = "Course schedules, Clash prevention, Classroom assignments")
public class TimetableController {

    private final TimetableService timetableService;

    public TimetableController(TimetableService timetableService) {
        this.timetableService = timetableService;
    }

    @GetMapping("/course/{courseId}/semester/{semester}")
    @Operation(summary = "Get course timetable by semester")
    public ResponseEntity<List<TimetableEntry>> getCourseTimetable(
            @PathVariable Long courseId,
            @PathVariable int semester) {
        return ResponseEntity.ok(timetableService.getCourseTimetable(courseId, semester));
    }

    @GetMapping("/lecturer/{lecturerId}")
    @Operation(summary = "Get lecturer's teaching timetable")
    public ResponseEntity<List<TimetableEntry>> getLecturerTimetable(@PathVariable Long lecturerId) {
        return ResponseEntity.ok(timetableService.getLecturerTimetable(lecturerId));
    }

    @GetMapping("/student/{studentId}")
    @Operation(summary = "Get student's enrolled weekly class schedule")
    public ResponseEntity<List<TimetableEntry>> getStudentTimetable(@PathVariable Long studentId) {
        return ResponseEntity.ok(timetableService.getStudentTimetable(studentId));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'LECTURER')")
    @Operation(summary = "Schedule class slot with clash detection")
    public ResponseEntity<TimetableEntry> createEntry(@Valid @RequestBody TimetableRequest request) {
        return ResponseEntity.ok(timetableService.createTimetableEntry(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'LECTURER')")
    @Operation(summary = "Update class slot with clash detection")
    public ResponseEntity<TimetableEntry> updateEntry(@PathVariable Long id, @Valid @RequestBody TimetableRequest request) {
        return ResponseEntity.ok(timetableService.updateTimetableEntry(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'LECTURER')")
    @Operation(summary = "Remove timetable slot")
    public ResponseEntity<?> deleteEntry(@PathVariable Long id) {
        timetableService.deleteTimetableEntry(id);
        return ResponseEntity.ok(Map.of("message", "Timetable slot deleted successfully"));
    }
}
