package com.investments.tracker.repository;

import com.investments.tracker.model.WeeklyOverview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeeklyOverviewRepository extends JpaRepository<WeeklyOverview, Long> {
}
