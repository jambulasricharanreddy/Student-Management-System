package com.smartstudenthub.service;

import com.smartstudenthub.dto.AssessmentRequest;
import com.smartstudenthub.dto.MarkEntryDto;
import com.smartstudenthub.entity.*;
import com.smartstudenthub.exception.BadRequestException;
import com.smartstudenthub.exception.ResourceNotFoundException;
import com.smartstudenthub.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class MarksService {

    private final AssessmentRepository assessmentRepository;
    private final MarkRepository markRepository;
    private final CourseRepository courseRepository;
    private final SubjectRepository subjectRepository;
    private final LecturerRepository lecturerRepository;
    private final StudentRepository studentRepository;
    private final AttendanceRecordRepository attendanceRecordRepository;

    public MarksService(
            AssessmentRepository assessmentRepository,
            MarkRepository markRepository,
            CourseRepository courseRepository,
            SubjectRepository subjectRepository,
            LecturerRepository lecturerRepository,
            StudentRepository studentRepository,
            AttendanceRecordRepository attendanceRecordRepository) {
        this.assessmentRepository = assessmentRepository;
        this.markRepository = markRepository;
        this.courseRepository = courseRepository;
        this.subjectRepository = subjectRepository;
        this.lecturerRepository = lecturerRepository;
        this.studentRepository = studentRepository;
        this.attendanceRecordRepository = attendanceRecordRepository;
    }

    @Transactional
    public Assessment createAssessment(Long lecturerId, AssessmentRequest request) {
        Lecturer lecturer = lecturerRepository.findById(lecturerId)
                .orElseThrow(() -> new ResourceNotFoundException("Lecturer not found"));
        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        Subject subject = subjectRepository.findById(request.getSubjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found"));

        if (request.getMaxMarks() == null || request.getMaxMarks().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Maximum marks must be greater than zero");
        }

        Assessment assessment = new Assessment();
        assessment.setTitle(request.getTitle());
        assessment.setCourse(course);
        assessment.setSubject(subject);
        assessment.setLecturer(lecturer);
        assessment.setType(request.getType());
        assessment.setMaxMarks(request.getMaxMarks());
        assessment.setWeightagePercent(request.getWeightagePercent() != null ? request.getWeightagePercent() : new BigDecimal("100.00"));
        assessment.setAcademicYear(request.getAcademicYear());
        assessment.setSemester(request.getSemester());
        assessment.setPublished(false);

        return assessmentRepository.save(assessment);
    }

    @Transactional
    public List<Mark> saveMarks(Long assessmentId, List<MarkEntryDto> entries, String gradedBy) {
        Assessment assessment = assessmentRepository.findById(assessmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Assessment not found"));

        List<Mark> saved = new ArrayList<>();
        for (MarkEntryDto entry : entries) {
            if (entry.getMarksObtained().compareTo(assessment.getMaxMarks()) > 0) {
                throw new BadRequestException("Marks obtained (" + entry.getMarksObtained() + ") cannot exceed maximum marks (" + assessment.getMaxMarks() + ")");
            }
            if (entry.getMarksObtained().compareTo(BigDecimal.ZERO) < 0) {
                throw new BadRequestException("Marks obtained cannot be negative");
            }

            Student student = studentRepository.findById(entry.getStudentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + entry.getStudentId()));

            String grade = entry.getGrade();
            if (grade == null || grade.trim().isEmpty()) {
                grade = calculateGrade(entry.getMarksObtained(), assessment.getMaxMarks());
            }

            Mark mark = markRepository.findByAssessmentIdAndStudentId(assessment.getId(), student.getId())
                    .orElseGet(Mark::new);

            mark.setAssessment(assessment);
            mark.setStudent(student);
            mark.setMarksObtained(entry.getMarksObtained());
            mark.setGrade(grade);
            mark.setRemarks(entry.getRemarks());
            mark.setGradedBy(gradedBy);
            mark.setLastModifiedBy(gradedBy);
            mark.setLastModifiedAt(LocalDateTime.now());

            saved.add(markRepository.save(mark));
        }
        return saved;
    }

    @Transactional
    public Assessment setPublishedStatus(Long assessmentId, boolean published) {
        Assessment assessment = assessmentRepository.findById(assessmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Assessment not found"));
        assessment.setPublished(published);
        return assessmentRepository.save(assessment);
    }

    public List<Assessment> getAssessmentsByCourse(Long courseId) {
        return assessmentRepository.findByCourseId(courseId);
    }

    public List<Mark> getMarksForAssessment(Long assessmentId) {
        return markRepository.findByAssessmentId(assessmentId);
    }

    public List<Mark> getStudentPublishedMarks(Long studentId) {
        return markRepository.findPublishedMarksByStudentId(studentId);
    }

    public Map<String, Object> getStudentPerformanceReport(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        List<Mark> marks = markRepository.findPublishedMarksByStudentId(studentId);

        BigDecimal totalObtained = BigDecimal.ZERO;
        BigDecimal totalMax = BigDecimal.ZERO;

        for (Mark m : marks) {
            totalObtained = totalObtained.add(m.getMarksObtained());
            totalMax = totalMax.add(m.getAssessment().getMaxMarks());
        }

        BigDecimal percentage = BigDecimal.ZERO;
        if (totalMax.compareTo(BigDecimal.ZERO) > 0) {
            percentage = totalObtained.multiply(new BigDecimal("100")).divide(totalMax, 2, RoundingMode.HALF_UP);
        }

        // Attendance stats
        long present = attendanceRecordRepository.countPresentByStudentId(studentId);
        long totalAttendance = attendanceRecordRepository.countTotalByStudentId(studentId);
        double attPercentage = totalAttendance > 0 ? ((double) present / totalAttendance) * 100.0 : 0.0;

        List<String> warnings = new ArrayList<>();
        if (totalAttendance >= 5 && attPercentage < 75.0) {
            warnings.add("Attendance Alert: Current attendance is " + Math.round(attPercentage) + "% (minimum 75% required).");
        }
        if (totalMax.compareTo(BigDecimal.ZERO) > 0 && percentage.compareTo(new BigDecimal("40.00")) < 0) {
            warnings.add("Academic Warning: Aggregate score is below 40% passing threshold.");
        }

        Map<String, Object> report = new HashMap<>();
        report.put("studentId", student.getId());
        report.put("fullName", student.getUser().getFullName());
        report.put("registrationNumber", student.getRegistrationNumber());
        report.put("totalMarksObtained", totalObtained);
        report.put("totalMaxMarks", totalMax);
        report.put("percentage", percentage);
        report.put("attendancePercentage", Math.round(attPercentage * 10.0) / 10.0);
        report.put("academicWarnings", warnings);
        report.put("marksList", marks);

        return report;
    }

    private String calculateGrade(BigDecimal obtained, BigDecimal max) {
        if (max.compareTo(BigDecimal.ZERO) == 0) return "N/A";
        BigDecimal pct = obtained.multiply(new BigDecimal("100")).divide(max, 2, RoundingMode.HALF_UP);

        if (pct.compareTo(new BigDecimal("90")) >= 0) return "A+";
        if (pct.compareTo(new BigDecimal("80")) >= 0) return "A";
        if (pct.compareTo(new BigDecimal("70")) >= 0) return "B+";
        if (pct.compareTo(new BigDecimal("60")) >= 0) return "B";
        if (pct.compareTo(new BigDecimal("50")) >= 0) return "C";
        if (pct.compareTo(new BigDecimal("40")) >= 0) return "D";
        return "F";
    }
}
