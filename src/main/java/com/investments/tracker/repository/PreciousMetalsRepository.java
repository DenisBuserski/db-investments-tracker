package com.investments.tracker.repository;

import com.investments.tracker.model.PreciousMetal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PreciousMetalsRepository extends JpaRepository<PreciousMetal, Long> {
}
