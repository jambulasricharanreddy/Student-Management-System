package com.smartstudenthub.repository;

import com.smartstudenthub.entity.Grievance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface GrievanceRepository extends JpaRepository<Grievance, Long> {
    Optional<Grievance> findByTicketNumber(String ticketNumber);
    List<Grievance> findByStudentId(Long studentId);
    Page<Grievance> findByStudentId(Long studentId, Pageable pageable);
    List<Grievance> findByStatus(String status);
    long countByStatus(String status);
}
