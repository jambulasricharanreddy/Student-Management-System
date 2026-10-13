package com.smartstudenthub.repository;

import com.smartstudenthub.entity.CampusLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CampusLocationRepository extends JpaRepository<CampusLocation, Long> {
    List<CampusLocation> findByIsActiveTrue();
    List<CampusLocation> findByCategoryAndIsActiveTrue(String category);
}
