package com.smartstudenthub.repository;

import com.smartstudenthub.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByIsActiveTrueOrderByEventDateAsc();
    List<Event> findByEventDateAfterAndIsActiveTrueOrderByEventDateAsc(LocalDateTime date);
    long countByEventDateAfterAndIsActiveTrue(LocalDateTime date);
}
