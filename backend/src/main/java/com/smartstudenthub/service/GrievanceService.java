package com.smartstudenthub.service;

import com.smartstudenthub.dto.GrievanceRequest;
import com.smartstudenthub.dto.GrievanceStatusUpdateRequest;
import com.smartstudenthub.entity.*;
import com.smartstudenthub.exception.BadRequestException;
import com.smartstudenthub.exception.ResourceNotFoundException;
import com.smartstudenthub.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class GrievanceService {

    private final GrievanceRepository grievanceRepository;
    private final GrievanceCommentRepository commentRepository;
    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    public GrievanceService(
            GrievanceRepository grievanceRepository,
            GrievanceCommentRepository commentRepository,
            StudentRepository studentRepository,
            UserRepository userRepository,
            NotificationService notificationService) {
        this.grievanceRepository = grievanceRepository;
        this.commentRepository = commentRepository;
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
    }

    @Transactional
    public Grievance submitGrievance(Long studentId, GrievanceRequest request) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        String ticketNumber = "GRV-" + LocalDateTime.now().getYear() + "-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();

        Grievance grievance = new Grievance();
        grievance.setTicketNumber(ticketNumber);
        grievance.setStudent(student);
        grievance.setCategory(request.getCategory().toUpperCase());
        grievance.setSubject(request.getSubject());
        grievance.setDescription(request.getDescription());
        grievance.setPriority(request.getPriority() != null ? request.getPriority().toUpperCase() : "MEDIUM");
        grievance.setStatus("SUBMITTED");

        grievance = grievanceRepository.save(grievance);

        notificationService.sendInAppNotification(
                student.getUser(),
                "Grievance Submitted",
                "Your grievance ticket " + ticketNumber + " has been logged and is awaiting review.",
                "INFO",
                "/grievances/" + grievance.getId()
        );

        return grievance;
    }

    public Page<Grievance> getStudentGrievances(Long studentId, Pageable pageable) {
        return grievanceRepository.findByStudentId(studentId, pageable);
    }

    public List<Grievance> getAllGrievances() {
        return grievanceRepository.findAll();
    }

    public Grievance getGrievanceById(Long id) {
        return grievanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Grievance ticket not found"));
    }

    @Transactional
    public Grievance updateStatus(Long grievanceId, GrievanceStatusUpdateRequest request, User currentUser) {
        Grievance grievance = getGrievanceById(grievanceId);

        String previousStatus = grievance.getStatus();
        grievance.setStatus(request.getStatus().toUpperCase());

        if (request.getAssignedToUserId() != null) {
            User assignee = userRepository.findById(request.getAssignedToUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("Assignee user not found"));
            grievance.setAssignedTo(assignee);
        }

        if (request.getResolutionNotes() != null) {
            grievance.setResolutionNotes(request.getResolutionNotes());
        }

        if ("RESOLVED".equalsIgnoreCase(request.getStatus())) {
            grievance.setResolvedAt(LocalDateTime.now());
        }

        if (request.getComment() != null && !request.getComment().trim().isEmpty()) {
            GrievanceComment comment = new GrievanceComment(
                    grievance,
                    currentUser,
                    request.getComment(),
                    request.isInternalComment()
            );
            commentRepository.save(comment);
        }

        grievance = grievanceRepository.save(grievance);

        if (!previousStatus.equalsIgnoreCase(grievance.getStatus())) {
            notificationService.sendInAppNotification(
                    grievance.getStudent().getUser(),
                    "Grievance Status Update: " + grievance.getTicketNumber(),
                    "Your grievance status has been changed to " + grievance.getStatus() + ".",
                    "ALERT",
                    "/grievances/" + grievance.getId()
            );
        }

        return grievance;
    }

    @Transactional
    public GrievanceComment addComment(Long grievanceId, User user, String commentText, boolean isInternal) {
        Grievance grievance = getGrievanceById(grievanceId);
        GrievanceComment comment = new GrievanceComment(grievance, user, commentText, isInternal);
        return commentRepository.save(comment);
    }

    public List<GrievanceComment> getComments(Long grievanceId, boolean isStaff) {
        if (isStaff) {
            return commentRepository.findByGrievanceIdOrderByCreatedAtAsc(grievanceId);
        } else {
            return commentRepository.findByGrievanceIdAndIsInternalFalseOrderByCreatedAtAsc(grievanceId);
        }
    }
}
