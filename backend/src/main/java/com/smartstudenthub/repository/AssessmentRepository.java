package com.smartstudenthub.repository;

import com.smartstudenthub.entity.Assessment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AssessmentRepository extends JpaRepository<Assessment, Long> {
    List<Assessment> findByCourseId(Long courseId);
    List<Assessment> findBySubjectId(Long subjectId);
    List<Assessment> findByLecturerId(Long lecturerId);
    List<Assessment> findByCourseIdAndSemester(Long courseId, int semester);
    List<Assessment> findByCourseIdAndIsPublishedTrue(Long courseId);
}
