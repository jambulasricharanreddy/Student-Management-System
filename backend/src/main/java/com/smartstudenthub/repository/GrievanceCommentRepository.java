package com.smartstudenthub.repository;

import com.smartstudenthub.entity.GrievanceComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface GrievanceCommentRepository extends JpaRepository<GrievanceComment, Long> {
    List<GrievanceComment> findByGrievanceIdOrderByCreatedAtAsc(Long grievanceId);
    List<GrievanceComment> findByGrievanceIdAndIsInternalFalseOrderByCreatedAtAsc(Long grievanceId);
}
