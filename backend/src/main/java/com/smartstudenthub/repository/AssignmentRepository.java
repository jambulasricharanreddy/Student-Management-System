package com.smartstudenthub.repository;

import com.smartstudenthub.entity.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    List<Assignment> findByCourseId(Long courseId);
    List<Assignment> findBySubjectId(Long subjectId);
    List<Assignment> findByLecturerId(Long lecturerId);
    long countByDueDateAfter(LocalDateTime dateTime);
}
