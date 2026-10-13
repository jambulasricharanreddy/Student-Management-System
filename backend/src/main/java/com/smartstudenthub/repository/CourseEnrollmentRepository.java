package com.smartstudenthub.repository;

import com.smartstudenthub.entity.CourseEnrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CourseEnrollmentRepository extends JpaRepository<CourseEnrollment, Long> {
    List<CourseEnrollment> findByStudentId(Long studentId);
    List<CourseEnrollment> findByCourseId(Long courseId);
    Optional<CourseEnrollment> findByStudentIdAndCourseIdAndAcademicYearAndSemester(Long studentId, Long courseId, String academicYear, int semester);
    boolean existsByStudentIdAndCourseIdAndAcademicYearAndSemester(Long studentId, Long courseId, String academicYear, int semester);
    long countByCourseId(Long courseId);
}
