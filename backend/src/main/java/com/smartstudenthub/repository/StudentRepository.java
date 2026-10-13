package com.smartstudenthub.repository;

import com.smartstudenthub.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByUserId(Long userId);
    Optional<Student> findByRegistrationNumber(String registrationNumber);
    boolean existsByRegistrationNumber(String registrationNumber);

    List<Student> findByCourseId(Long courseId);
    List<Student> findByDepartmentId(Long departmentId);

    @Query("SELECT s FROM Student s WHERE " +
           "(:query IS NULL OR :query = '' OR LOWER(s.user.fullName) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(s.registrationNumber) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(s.user.email) LIKE LOWER(CONCAT('%', :query, '%'))) AND " +
           "(:departmentId IS NULL OR s.department.id = :departmentId) AND " +
           "(:courseId IS NULL OR s.course.id = :courseId) AND " +
           "(:semester IS NULL OR s.currentSemester = :semester) AND " +
           "(:academicYear IS NULL OR :academicYear = '' OR s.academicYear = :academicYear) AND " +
           "(:status IS NULL OR :status = '' OR s.status = :status)")
    Page<Student> searchStudents(
            @Param("query") String query,
            @Param("departmentId") Long departmentId,
            @Param("courseId") Long courseId,
            @Param("semester") Integer semester,
            @Param("academicYear") String academicYear,
            @Param("status") String status,
            Pageable pageable
    );

    long countByStatus(String status);
}
