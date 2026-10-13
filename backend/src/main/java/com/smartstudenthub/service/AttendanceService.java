package com.smartstudenthub.service;

import com.smartstudenthub.dto.AttendanceCheckInRequest;
import com.smartstudenthub.dto.AttendanceSessionRequest;
import com.smartstudenthub.dto.ManualAttendanceRequest;
import com.smartstudenthub.entity.*;
import com.smartstudenthub.exception.BadRequestException;
import com.smartstudenthub.exception.ConflictException;
import com.smartstudenthub.exception.ResourceNotFoundException;
import com.smartstudenthub.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class AttendanceService {

    private final AttendanceSessionRepository sessionRepository;
    private final AttendanceRecordRepository recordRepository;
    private final CourseRepository courseRepository;
    private final SubjectRepository subjectRepository;
    private final LecturerRepository lecturerRepository;
    private final StudentRepository studentRepository;
    private final CourseEnrollmentRepository enrollmentRepository;

    private final SecureRandom secureRandom = new SecureRandom();

    public AttendanceService(
            AttendanceSessionRepository sessionRepository,
            AttendanceRecordRepository recordRepository,
            CourseRepository courseRepository,
            SubjectRepository subjectRepository,
            LecturerRepository lecturerRepository,
            StudentRepository studentRepository,
            CourseEnrollmentRepository enrollmentRepository) {
        this.sessionRepository = sessionRepository;
        this.recordRepository = recordRepository;
        this.courseRepository = courseRepository;
        this.subjectRepository = subjectRepository;
        this.lecturerRepository = lecturerRepository;
        this.studentRepository = studentRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    @Transactional
    public AttendanceSession startSession(Long lecturerId, AttendanceSessionRequest request) {
        Lecturer lecturer = lecturerRepository.findById(lecturerId)
                .orElseThrow(() -> new ResourceNotFoundException("Lecturer not found"));
        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        Subject subject = subjectRepository.findById(request.getSubjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found"));

        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);
        String token = Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
        String tokenHash = hashToken(token);

        LocalDateTime now = LocalDateTime.now();
        int minutes = request.getDurationMinutes() > 0 ? request.getDurationMinutes() : 15;
        LocalDateTime expiresAt = now.plusMinutes(minutes);

        AttendanceSession session = new AttendanceSession();
        session.setSessionToken(token);
        session.setTokenHash(tokenHash);
        session.setCourse(course);
        session.setSubject(subject);
        session.setLecturer(lecturer);
        session.setSessionDate(LocalDate.now());
        session.setStartTime(now);
        session.setExpiresAt(expiresAt);
        session.setStatus("ACTIVE");

        return sessionRepository.save(session);
    }

    @Transactional
    public AttendanceRecord checkIn(Long studentId, AttendanceCheckInRequest request) {
        AttendanceSession session = sessionRepository.findBySessionToken(request.getSessionToken())
                .orElseThrow(() -> new ResourceNotFoundException("Invalid or expired attendance session"));

        if (!"ACTIVE".equalsIgnoreCase(session.getStatus())) {
            throw new BadRequestException("Attendance session is already closed");
        }

        if (session.getExpiresAt().isBefore(LocalDateTime.now())) {
            session.setStatus("EXPIRED");
            sessionRepository.save(session);
            throw new BadRequestException("Attendance session has expired");
        }

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        if (!student.getCourse().getId().equals(session.getCourse().getId())) {
            throw new BadRequestException("You are not enrolled in the course for this attendance session");
        }

        if (recordRepository.existsBySessionIdAndStudentId(session.getId(), student.getId())) {
            throw new ConflictException("Attendance already recorded for this session");
        }

        AttendanceRecord record = new AttendanceRecord(
                session,
                student,
                session.getCourse(),
                session.getSubject(),
                "PRESENT",
                "QR_SCAN"
        );
        return recordRepository.save(record);
    }

    @Transactional
    public AttendanceRecord manualAttendance(Long lecturerId, ManualAttendanceRequest request) {
        AttendanceSession session = sessionRepository.findById(request.getSessionId())
                .orElseThrow(() -> new ResourceNotFoundException("Session not found"));
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        Optional<AttendanceRecord> existing = recordRepository.findBySessionIdAndStudentId(session.getId(), student.getId());
        AttendanceRecord record;
        if (existing.isPresent()) {
            record = existing.get();
            record.setStatus(request.getStatus());
            record.setVerificationMode("MANUAL_UPDATE");
            record.setRemarks(request.getRemarks());
        } else {
            record = new AttendanceRecord(session, student, session.getCourse(), session.getSubject(), request.getStatus(), "MANUAL_ENTRY");
            record.setRemarks(request.getRemarks());
        }
        return recordRepository.save(record);
    }

    @Transactional
    public AttendanceSession closeSession(Long sessionId) {
        AttendanceSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found"));
        session.setStatus("CLOSED");
        return sessionRepository.save(session);
    }

    public List<AttendanceRecord> getSessionRecords(Long sessionId) {
        return recordRepository.findBySessionId(sessionId);
    }

    public Map<String, Object> getStudentAttendanceStats(Long studentId) {
        long present = recordRepository.countPresentByStudentId(studentId);
        long total = recordRepository.countTotalByStudentId(studentId);
        double percentage = total > 0 ? ((double) present / total) * 100.0 : 0.0;

        Map<String, Object> stats = new HashMap<>();
        stats.put("presentSessions", present);
        stats.put("totalSessions", total);
        stats.put("percentage", Math.round(percentage * 10.0) / 10.0);
        stats.put("isAtRisk", total >= 5 && percentage < 75.0);
        return stats;
    }

    public List<AttendanceRecord> getStudentAttendanceHistory(Long studentId) {
        return recordRepository.findByStudentId(studentId);
    }

    public List<AttendanceSession> getLecturerSessions(Long lecturerId) {
        return sessionRepository.findByLecturerId(lecturerId);
    }

    private String hashToken(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(token.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            return String.valueOf(token.hashCode());
        }
    }
}
