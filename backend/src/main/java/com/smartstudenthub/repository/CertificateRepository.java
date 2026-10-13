package com.smartstudenthub.repository;

import com.smartstudenthub.entity.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CertificateRepository extends JpaRepository<Certificate, Long> {
    Optional<Certificate> findByVerificationId(String verificationId);
    List<Certificate> findByStudentId(Long studentId);
    boolean existsByVerificationId(String verificationId);
}
