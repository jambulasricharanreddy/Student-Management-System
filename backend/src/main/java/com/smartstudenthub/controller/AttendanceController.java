package com.smartstudenthub.controller;

import com.smartstudenthub.dto.AttendanceCheckInRequest;
import com.smartstudenthub.dto.AttendanceSessionRequest;
import com.smartstudenthub.dto.ManualAttendanceRequest;
import com.smartstudenthub.entity.AttendanceRecord;
import com.smartstudenthub.entity.AttendanceSession;
import com.smartstudenthub.service.AttendanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/attendance")
@Tag(name = "Attendance Management", description = "QR-based attendance, Manual entries, and Analytics")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @PostMapping("/session/start/{lecturerId}")
    @PreAuthorize("hasAnyRole('LECTURER', 'ADMIN')")
    @Operation(summary = "Start dynamic QR attendance session")
    public ResponseEntity<AttendanceSession> startSession(
            @PathVariable Long lecturerId,
            @Valid @RequestBody AttendanceSessionRequest request) {
        return ResponseEntity.ok(attendanceService.startSession(lecturerId, request));
    }

    @PostMapping("/session/{id}/close")
    @PreAuthorize("hasAnyRole('LECTURER', 'ADMIN')")
    @Operation(summary = "Close active attendance session")
    public ResponseEntity<AttendanceSession> closeSession(@PathVariable Long id) {
        return ResponseEntity.ok(attendanceService.closeSession(id));
    }

    @PostMapping("/check-in/{studentId}")
    @PreAuthorize("hasRole('STUDENT')")
    @Operation(summary = "Student QR scan check-in")
    public ResponseEntity<AttendanceRecord> checkIn(
            @PathVariable Long studentId,
            @Valid @RequestBody AttendanceCheckInRequest request) {
        return ResponseEntity.ok(attendanceService.checkIn(studentId, request));
    }

    @PostMapping("/manual/{lecturerId}")
    @PreAuthorize("hasAnyRole('LECTURER', 'ADMIN')")
    @Operation(summary = "Lecturer manual attendance entry or override")
    public ResponseEntity<AttendanceRecord> manualAttendance(
            @PathVariable Long lecturerId,
            @Valid @RequestBody ManualAttendanceRequest request) {
        return ResponseEntity.ok(attendanceService.manualAttendance(lecturerId, request));
    }

    @GetMapping("/session/{sessionId}/records")
    @PreAuthorize("hasAnyRole('LECTURER', 'ADMIN')")
    @Operation(summary = "Get live attendance list for session")
    public ResponseEntity<List<AttendanceRecord>> getSessionRecords(@PathVariable Long sessionId) {
        return ResponseEntity.ok(attendanceService.getSessionRecords(sessionId));
    }

    @GetMapping("/student/{studentId}/stats")
    @Operation(summary = "Get student attendance percentage and warning indicators")
    public ResponseEntity<Map<String, Object>> getStudentStats(@PathVariable Long studentId) {
        return ResponseEntity.ok(attendanceService.getStudentAttendanceStats(studentId));
    }

    @GetMapping("/student/{studentId}/history")
    @Operation(summary = "Get student attendance check-in history")
    public ResponseEntity<List<AttendanceRecord>> getStudentHistory(@PathVariable Long studentId) {
        return ResponseEntity.ok(attendanceService.getStudentAttendanceHistory(studentId));
    }

    @GetMapping("/lecturer/{lecturerId}/sessions")
    @PreAuthorize("hasAnyRole('LECTURER', 'ADMIN')")
    @Operation(summary = "Get all attendance sessions created by lecturer")
    public ResponseEntity<List<AttendanceSession>> getLecturerSessions(@PathVariable Long lecturerId) {
        return ResponseEntity.ok(attendanceService.getLecturerSessions(lecturerId));
    }
}
