package com.smartstudenthub.repository;

import com.smartstudenthub.entity.LibraryLoan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LibraryLoanRepository extends JpaRepository<LibraryLoan, Long> {
    List<LibraryLoan> findByStudentId(Long studentId);
    List<LibraryLoan> findByBookId(Long bookId);
    List<LibraryLoan> findByStudentIdAndStatus(Long studentId, String status);
    long countByStudentIdAndStatus(Long studentId, String status);
    List<LibraryLoan> findByStatus(String status);
}
