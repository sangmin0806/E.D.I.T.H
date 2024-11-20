package com.edith.developmentassistant.infrastructure.repository.jpa;

import com.edith.developmentassistant.domain.model.MRSummary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MRSummaryRepository extends JpaRepository<MRSummary, Long> {
    List<MRSummary> findByProjectId(Long projectId);
    List<MRSummary> findTop10ByProjectIdOrderByCreatedDateDesc(Long projectId);
}
