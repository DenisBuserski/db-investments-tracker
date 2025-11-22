package com.investments.tracker.repository;

import com.investments.tracker.model.WeeklyPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeeklyPositionRepository extends JpaRepository<WeeklyPosition, Long> {
}
