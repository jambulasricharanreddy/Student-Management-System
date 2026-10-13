package com.smartstudenthub.service;

import com.smartstudenthub.dto.AssignmentRequest;
import com.smartstudenthub.entity.*;
import com.smartstudenthub.exception.BadRequestException;
import com.smartstudenthub.exception.ResourceNotFoundException;
import com.smartstudenthub.repository.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final AssignmentSubmissionRepository submissionRepository;
    private final CourseRepository courseRepository;
    private final SubjectRepository subjectRepository;
    private final LecturerRepository lecturerRepository;
    private final StudentRepository studentRepository;

    @Value("${app.upload.dir:./uploads}")
    private String uploadDir;

    private static final List<String> DISALLOWED_EXTENSIONS = Arrays.asList(
            ".exe", ".bat", ".cmd", ".sh", ".msi", ".jar", ".jsp", ".jspx", ".php", ".phtml", ".vbs", ".dll"
    );

    public AssignmentService(
            AssignmentRepository assignmentRepository,
            AssignmentSubmissionRepository submissionRepository,
            CourseRepository courseRepository,
            SubjectRepository subjectRepository,
            LecturerRepository lecturerRepository,
            StudentRepository studentRepository) {
        this.assignmentRepository = assignmentRepository;
        this.submissionRepository = submissionRepository;
        this.courseRepository = courseRepository;
        this.subjectRepository = subjectRepository;
        this.lecturerRepository = lecturerRepository;
        this.studentRepository = studentRepository;
    }

    @Transactional
    public Assignment createAssignment(Long lecturerId, AssignmentRequest request, MultipartFile attachment) {
        Lecturer lecturer = lecturerRepository.findById(lecturerId)
                .orElseThrow(() -> new ResourceNotFoundException("Lecturer not found"));
        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        Subject subject = subjectRepository.findById(request.getSubjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found"));

        Assignment assignment = new Assignment();
        assignment.setTitle(request.getTitle());
        assignment.setDescription(request.getDescription());
        assignment.setCourse(course);
        assignment.setSubject(subject);
        assignment.setLecturer(lecturer);
        assignment.setDueDate(request.getDueDate());
        assignment.setMaxMarks(request.getMaxMarks() != null ? request.getMaxMarks() : new BigDecimal("100.00"));

        if (attachment != null && !attachment.isEmpty()) {
            String savedPath = storeFile(attachment, "assignment-materials");
            assignment.setAttachmentPath(savedPath);
        }

        return assignmentRepository.save(assignment);
    }

    @Transactional
    public AssignmentSubmission submitAssignment(Long studentId, Long assignmentId, MultipartFile file) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not found"));

        if (file == null || file.isEmpty()) {
            throw new BadRequestException("Please select a file to submit");
        }

        String storedPath = storeFile(file, "submissions");
        LocalDateTime now = LocalDateTime.now();
        boolean isLate = now.isAfter(assignment.getDueDate());

        AssignmentSubmission submission = submissionRepository.findByAssignmentIdAndStudentId(assignment.getId(), student.getId())
                .orElseGet(AssignmentSubmission::new);

        submission.setAssignment(assignment);
        submission.setStudent(student);
        submission.setSubmittedAt(now);
        submission.setFileName(StringUtils.cleanPath(file.getOriginalFilename() != null ? file.getOriginalFilename() : "submission"));
        submission.setFilePath(storedPath);
        submission.setFileType(file.getContentType());
        submission.setFileSize(file.getSize());
        submission.setLate(isLate);
        submission.setStatus(isLate ? "LATE" : "SUBMITTED");

        return submissionRepository.save(submission);
    }

    @Transactional
    public AssignmentSubmission gradeSubmission(Long submissionId, BigDecimal marks, String feedback) {
        AssignmentSubmission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new ResourceNotFoundException("Submission not found"));

        if (marks != null && marks.compareTo(submission.getAssignment().getMaxMarks()) > 0) {
            throw new BadRequestException("Marks awarded cannot exceed maximum marks (" + submission.getAssignment().getMaxMarks() + ")");
        }

        submission.setMarksAwarded(marks);
        submission.setFeedback(feedback);
        submission.setStatus("GRADED");

        return submissionRepository.save(submission);
    }

    public List<Assignment> getAssignmentsByCourse(Long courseId) {
        return assignmentRepository.findByCourseId(courseId);
    }

    public List<Assignment> getAssignmentsByLecturer(Long lecturerId) {
        return assignmentRepository.findByLecturerId(lecturerId);
    }

    public List<AssignmentSubmission> getSubmissionsForAssignment(Long assignmentId) {
        return submissionRepository.findByAssignmentId(assignmentId);
    }

    public List<AssignmentSubmission> getStudentSubmissions(Long studentId) {
        return submissionRepository.findByStudentId(studentId);
    }

    private String storeFile(MultipartFile file, String subDir) {
        String originalName = file.getOriginalFilename();
        if (originalName != null) {
            String lower = originalName.toLowerCase();
            for (String disallowed : DISALLOWED_EXTENSIONS) {
                if (lower.endsWith(disallowed)) {
                    throw new BadRequestException("Executable and scripting file types (" + disallowed + ") are strictly prohibited for safety.");
                }
            }
        }

        try {
            Path targetDir = Paths.get(uploadDir, subDir).toAbsolutePath().normalize();
            if (!Files.exists(targetDir)) {
                Files.createDirectories(targetDir);
            }

            String ext = "";
            if (originalName != null && originalName.contains(".")) {
                ext = originalName.substring(originalName.lastIndexOf("."));
            }
            String uniqueName = UUID.randomUUID() + ext;
            Path targetFile = targetDir.resolve(uniqueName);

            Files.copy(file.getInputStream(), targetFile, StandardCopyOption.REPLACE_EXISTING);
            return "/uploads/" + subDir + "/" + uniqueName;
        } catch (IOException e) {
            throw new BadRequestException("Failed to store file: " + e.getMessage());
        }
    }
}
