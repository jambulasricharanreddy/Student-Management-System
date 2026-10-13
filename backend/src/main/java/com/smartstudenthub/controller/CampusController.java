package com.smartstudenthub.controller;

import com.smartstudenthub.dto.CampusLocationRequest;
import com.smartstudenthub.entity.CampusLocation;
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
@RequestMapping("/api/campus")
@Tag(name = "Campus Navigation", description = "GIS Locations, Buildings, Labs, Facilities")
public class CampusController {

    private final CampusEventService campusEventService;

    public CampusController(CampusEventService campusEventService) {
        this.campusEventService = campusEventService;
    }

    @GetMapping("/locations")
    @Operation(summary = "Get active campus locations for interactive map")
    public ResponseEntity<List<CampusLocation>> getActiveLocations() {
        return ResponseEntity.ok(campusEventService.getActiveLocations());
    }

    @GetMapping("/locations/all")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all campus locations including inactive")
    public ResponseEntity<List<CampusLocation>> getAllLocations() {
        return ResponseEntity.ok(campusEventService.getAllLocations());
    }

    @PostMapping("/locations")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Add new campus location (Admin only)")
    public ResponseEntity<CampusLocation> createLocation(@Valid @RequestBody CampusLocationRequest request) {
        return ResponseEntity.ok(campusEventService.createLocation(request));
    }

    @PutMapping("/locations/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update campus location coordinates or details (Admin only)")
    public ResponseEntity<CampusLocation> updateLocation(@PathVariable Long id, @Valid @RequestBody CampusLocationRequest request) {
        return ResponseEntity.ok(campusEventService.updateLocation(id, request));
    }

    @DeleteMapping("/locations/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Remove campus location")
    public ResponseEntity<?> deleteLocation(@PathVariable Long id) {
        campusEventService.deleteLocation(id);
        return ResponseEntity.ok(Map.of("message", "Location removed successfully"));
    }
}
