package com.smartstudenthub.repository;

import com.smartstudenthub.entity.StudentFee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.List;

@Repository
public interface StudentFeeRepository extends JpaRepository<StudentFee, Long> {
    List<StudentFee> findByStudentId(Long studentId);
    List<StudentFee> findByStatus(String status);

    @Query("SELECT COALESCE(SUM(sf.amountDue), 0) FROM StudentFee sf")
    BigDecimal getTotalAmountDue();

    @Query("SELECT COALESCE(SUM(sf.amountPaid), 0) FROM StudentFee sf")
    BigDecimal getTotalAmountPaid();

    @Query("SELECT COALESCE(SUM(sf.balance), 0) FROM StudentFee sf")
    BigDecimal getTotalBalance();
}
