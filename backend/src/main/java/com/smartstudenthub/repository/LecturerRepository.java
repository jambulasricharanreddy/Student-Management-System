package com.smartstudenthub.repository;

import com.smartstudenthub.entity.Lecturer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface LecturerRepository extends JpaRepository<Lecturer, Long> {
    Optional<Lecturer> findByUserId(Long userId);
    Optional<Lecturer> findByEmployeeId(String employeeId);
    boolean existsByEmployeeId(String employeeId);
    List<Lecturer> findByDepartmentId(Long departmentId);
}
