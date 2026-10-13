package com.smartstudenthub.repository;

import com.smartstudenthub.entity.TimetableEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface TimetableEntryRepository extends JpaRepository<TimetableEntry, Long> {
    List<TimetableEntry> findByCourseIdAndSemester(Long courseId, int semester);
    List<TimetableEntry> findByLecturerId(Long lecturerId);
    List<TimetableEntry> findByClassroom(String classroom);

    @Query("SELECT t FROM TimetableEntry t WHERE " +
           "t.dayOfWeek = :dayOfWeek AND " +
           "(:excludeId IS NULL OR t.id <> :excludeId) AND " +
           "(t.startTime < :endTime AND t.endTime > :startTime) AND " +
           "(t.classroom = :classroom OR t.lecturer.id = :lecturerId OR (t.course.id = :courseId AND t.semester = :semester))")
    List<TimetableEntry> findClashes(
            @Param("dayOfWeek") String dayOfWeek,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime,
            @Param("classroom") String classroom,
            @Param("lecturerId") Long lecturerId,
            @Param("courseId") Long courseId,
            @Param("semester") int semester,
            @Param("excludeId") Long excludeId
    );
}
