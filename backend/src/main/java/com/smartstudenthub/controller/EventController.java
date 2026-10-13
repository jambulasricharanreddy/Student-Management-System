package com.smartstudenthub.controller;

import com.smartstudenthub.dto.EventRequest;
import com.smartstudenthub.entity.Event;
import com.smartstudenthub.entity.EventRegistration;
import com.smartstudenthub.entity.User;
import com.smartstudenthub.service.AuthService;
import com.smartstudenthub.service.CampusEventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/events")
@Tag(name = "Event Management", description = "Events, Capacity-safe registrations, and QR tickets")
public class EventController {

    private final CampusEventService campusEventService;
    private final AuthService authService;

    public EventController(CampusEventService campusEventService, AuthService authService) {
        this.campusEventService = campusEventService;
        this.authService = authService;
    }

    @GetMapping
    @Operation(summary = "Get all active upcoming events")
    public ResponseEntity<List<Event>> getUpcomingEvents() {
        return ResponseEntity.ok(campusEventService.getUpcomingEvents());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get event details by ID")
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        return ResponseEntity.ok(campusEventService.getEventById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'LECTURER')")
    @Operation(summary = "Create campus event with capacity constraint")
    public ResponseEntity<Event> createEvent(@Valid @RequestBody EventRequest request) {
        User user = authService.getCurrentUser();
        return ResponseEntity.ok(campusEventService.createEvent(request, user));
    }

    @PostMapping("/{id}/register/{studentId}")
    @PreAuthorize("hasRole('STUDENT')")
    @Operation(summary = "Register student for event with concurrency-safe capacity check")
    public ResponseEntity<EventRegistration> registerForEvent(
            @PathVariable Long id,
            @PathVariable Long studentId) {
        return ResponseEntity.ok(campusEventService.registerStudentForEvent(studentId, id));
    }

    @PostMapping("/check-in")
    @PreAuthorize("hasAnyRole('ADMIN', 'LECTURER')")
    @Operation(summary = "Verify and check in event attendee by ticket QR code")
    public ResponseEntity<EventRegistration> checkInAttendee(@RequestBody Map<String, String> body) {
        return ResponseEntity.ok(campusEventService.checkInTicket(body.get("ticketQrCode")));
    }

    @GetMapping("/student/{studentId}/registrations")
    @Operation(summary = "Get student event registrations and tickets")
    public ResponseEntity<List<EventRegistration>> getStudentRegistrations(@PathVariable Long studentId) {
        return ResponseEntity.ok(campusEventService.getStudentEventRegistrations(studentId));
    }

    @GetMapping("/{id}/registrations")
    @PreAuthorize("hasAnyRole('ADMIN', 'LECTURER')")
    @Operation(summary = "Get all registered participants for an event")
    public ResponseEntity<List<EventRegistration>> getEventRegistrations(@PathVariable Long id) {
        return ResponseEntity.ok(campusEventService.getEventRegistrations(id));
    }
}
