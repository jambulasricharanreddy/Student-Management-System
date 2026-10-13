package com.smartstudenthub.controller;

import com.smartstudenthub.entity.*;
import com.smartstudenthub.service.AcademicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/academics")
@Tag(name = "Academic Administration", description = "Departments, Courses, Subjects, Enrollments")
public class AcademicController {

    private final AcademicService academicService;

    public AcademicController(AcademicService academicService) {
        this.academicService = academicService;
    }

    // Departments
    @GetMapping("/departments")
    @Operation(summary = "Get all departments")
    public ResponseEntity<List<Department>> getDepartments() {
        return ResponseEntity.ok(academicService.getAllDepartments());
    }

    @PostMapping("/departments")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create department (Admin only)")
    public ResponseEntity<Department> createDepartment(@Valid @RequestBody Department department) {
        return ResponseEntity.ok(academicService.createDepartment(department));
    }

    // Courses
    @GetMapping("/courses")
    @Operation(summary = "Get all courses")
    public ResponseEntity<List<Course>> getCourses(@RequestParam(required = false) Long departmentId) {
        if (departmentId != null) {
            return ResponseEntity.ok(academicService.getCoursesByDepartment(departmentId));
        }
        return ResponseEntity.ok(academicService.getAllCourses());
    }

    @PostMapping("/courses")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create course (Admin only)")
    public ResponseEntity<Course> createCourse(@Valid @RequestBody Course course) {
        return ResponseEntity.ok(academicService.createCourse(course));
    }

    // Subjects
    @GetMapping("/subjects")
    @Operation(summary = "Get all subjects")
    public ResponseEntity<List<Subject>> getSubjects(
            @RequestParam(required = false) Long courseId,
            @RequestParam(required = false) Integer semester) {
        if (courseId != null && semester != null) {
            return ResponseEntity.ok(academicService.getSubjectsByCourseAndSemester(courseId, semester));
        } else if (courseId != null) {
            return ResponseEntity.ok(academicService.getSubjectsByCourse(courseId));
        }
        return ResponseEntity.ok(academicService.getAllSubjects());
    }

    @PostMapping("/subjects")
    @PreAuthorize("hasAnyRole('ADMIN', 'LECTURER')")
    @Operation(summary = "Create subject")
    public ResponseEntity<Subject> createSubject(@Valid @RequestBody Subject subject) {
        return ResponseEntity.ok(academicService.createSubject(subject));
    }

    // Lecturers
    @GetMapping("/lecturers")
    @Operation(summary = "Get all lecturers")
    public ResponseEntity<List<Lecturer>> getLecturers() {
        return ResponseEntity.ok(academicService.getAllLecturers());
    }

    @GetMapping("/lecturers/{id}")
    @Operation(summary = "Get lecturer details by ID")
    public ResponseEntity<Lecturer> getLecturerById(@PathVariable Long id) {
        return ResponseEntity.ok(academicService.getLecturerById(id));
    }

    @PostMapping("/lecturers/assign-course")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Assign lecturer to course (Admin only)")
    public ResponseEntity<Lecturer> assignLecturerCourse(@RequestBody Map<String, Long> body) {
        return ResponseEntity.ok(academicService.assignLecturerToCourse(body.get("lecturerId"), body.get("courseId")));
    }

    // Course Enrollments
    @GetMapping("/enrollments/student/{studentId}")
    @Operation(summary = "Get student course enrollments")
    public ResponseEntity<List<CourseEnrollment>> getStudentEnrollments(@PathVariable Long studentId) {
        return ResponseEntity.ok(academicService.getStudentEnrollments(studentId));
    }

    @PostMapping("/enrollments")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Enroll student into course (Admin only)")
    public ResponseEntity<CourseEnrollment> enrollStudent(@RequestBody Map<String, Object> body) {
        Long studentId = Long.valueOf(body.get("studentId").toString());
        Long courseId = Long.valueOf(body.get("courseId").toString());
        String academicYear = body.get("academicYear").toString();
        int semester = Integer.parseInt(body.get("semester").toString());

        return ResponseEntity.ok(academicService.enrollStudent(studentId, courseId, academicYear, semester));
    }
}
