package com.smartstudenthub.repository;

import com.smartstudenthub.entity.AttendanceSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceSessionRepository extends JpaRepository<AttendanceSession, Long> {
    Optional<AttendanceSession> findBySessionToken(String sessionToken);
    List<AttendanceSession> findByCourseId(Long courseId);
    List<AttendanceSession> findByLecturerId(Long lecturerId);
    List<AttendanceSession> findBySessionDate(LocalDate sessionDate);
}
