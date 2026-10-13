package com.smartstudenthub.repository;

import com.smartstudenthub.entity.InternshipApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface InternshipApplicationRepository extends JpaRepository<InternshipApplication, Long> {
    List<InternshipApplication> findByStudentId(Long studentId);
    List<InternshipApplication> findByInternshipId(Long internshipId);
    Optional<InternshipApplication> findByInternshipIdAndStudentId(Long internshipId, Long studentId);
    boolean existsByInternshipIdAndStudentId(Long internshipId, Long studentId);
}
