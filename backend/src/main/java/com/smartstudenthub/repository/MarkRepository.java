package com.smartstudenthub.repository;

import com.smartstudenthub.entity.Mark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface MarkRepository extends JpaRepository<Mark, Long> {
    List<Mark> findByAssessmentId(Long assessmentId);
    List<Mark> findByStudentId(Long studentId);
    Optional<Mark> findByAssessmentIdAndStudentId(Long assessmentId, Long studentId);
    boolean existsByAssessmentIdAndStudentId(Long assessmentId, Long studentId);

    @Query("SELECT m FROM Mark m WHERE m.student.id = :studentId AND m.assessment.isPublished = true")
    List<Mark> findPublishedMarksByStudentId(@Param("studentId") Long studentId);
}
