package com.investments.tracker.repository;

import com.investments.tracker.model.PreciousMetals;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PreciousMetalsRepository extends JpaRepository<PreciousMetals, Long> {
}
