package com.smartstudenthub.service;

import com.smartstudenthub.dto.InternshipRequest;
import com.smartstudenthub.entity.*;
import com.smartstudenthub.exception.BadRequestException;
import com.smartstudenthub.exception.ConflictException;
import com.smartstudenthub.exception.ResourceNotFoundException;
import com.smartstudenthub.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InternshipService {

    private final InternshipRepository internshipRepository;
    private final InternshipApplicationRepository applicationRepository;
    private final ResumeProfileRepository resumeProfileRepository;
    private final StudentRepository studentRepository;

    public InternshipService(
            InternshipRepository internshipRepository,
            InternshipApplicationRepository applicationRepository,
            ResumeProfileRepository resumeProfileRepository,
            StudentRepository studentRepository) {
        this.internshipRepository = internshipRepository;
        this.applicationRepository = applicationRepository;
        this.resumeProfileRepository = resumeProfileRepository;
        this.studentRepository = studentRepository;
    }

    public List<Internship> getAllOpenInternships() {
        return internshipRepository.findByStatusOrderByCreatedAtDesc("OPEN");
    }

    public List<Internship> getAllInternships() {
        return internshipRepository.findAll();
    }

    public Internship getInternshipById(Long id) {
        return internshipRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Internship listing not found"));
    }

    @Transactional
    public Internship createInternship(InternshipRequest request, User poster) {
        Internship internship = new Internship();
        internship.setTitle(request.getTitle());
        internship.setCompany(request.getCompany());
        internship.setDescription(request.getDescription());
        internship.setRequiredSkills(request.getRequiredSkills());
        internship.setEligibilityCriteria(request.getEligibilityCriteria());
        internship.setLocation(request.getLocation());
        internship.setWorkMode(request.getWorkMode());
        internship.setStipend(request.getStipend());
        internship.setDeadline(request.getDeadline());
        internship.setApplyUrl(request.getApplyUrl());
        internship.setInternal(request.isInternal());
        internship.setStatus("OPEN");
        internship.setPostedBy(poster);

        return internshipRepository.save(internship);
    }

    @Transactional
    public InternshipApplication apply(Long studentId, Long internshipId, String coverLetter, String resumePath) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
        Internship internship = getInternshipById(internshipId);

        if (!"OPEN".equalsIgnoreCase(internship.getStatus())) {
            throw new BadRequestException("This internship application is closed");
        }

        if (applicationRepository.existsByInternshipIdAndStudentId(internship.getId(), student.getId())) {
            throw new ConflictException("You have already submitted an application for this position");
        }

        InternshipApplication app = new InternshipApplication();
        app.setInternship(internship);
        app.setStudent(student);
        app.setCoverLetter(coverLetter);
        app.setResumePath(resumePath);
        app.setStatus("APPLIED");

        return applicationRepository.save(app);
    }

    public List<InternshipApplication> getStudentApplications(Long studentId) {
        return applicationRepository.findByStudentId(studentId);
    }

    public List<InternshipApplication> getInternshipApplications(Long internshipId) {
        return applicationRepository.findByInternshipId(internshipId);
    }

    @Transactional
    public InternshipApplication updateApplicationStatus(Long applicationId, String status, String reviewNotes) {
        InternshipApplication app = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));
        app.setStatus(status);
        app.setReviewNotes(reviewNotes);
        return applicationRepository.save(app);
    }

    // Resume Profile Management
    public ResumeProfile getResumeProfile(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        return resumeProfileRepository.findByStudentId(studentId)
                .orElseGet(() -> {
                    ResumeProfile profile = new ResumeProfile();
                    profile.setStudent(student);
                    return resumeProfileRepository.save(profile);
                });
    }

    @Transactional
    public ResumeProfile saveResumeProfile(Long studentId, ResumeProfile updated) {
        ResumeProfile existing = getResumeProfile(studentId);
        existing.setCareerObjective(updated.getCareerObjective());
        existing.setSkills(updated.getSkills());
        existing.setEducationJson(updated.getEducationJson());
        existing.setExperienceJson(updated.getExperienceJson());
        existing.setProjectsJson(updated.getProjectsJson());
        existing.setCertificationsJson(updated.getCertificationsJson());
        existing.setAchievementsJson(updated.getAchievementsJson());
        if (updated.getTemplateName() != null) {
            existing.setTemplateName(updated.getTemplateName());
        }
        return resumeProfileRepository.save(existing);
    }
}
