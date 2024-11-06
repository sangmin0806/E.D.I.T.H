package com.edith.developmentassistant.repository;

import com.edith.developmentassistant.domain.MRSummary;
import com.edith.developmentassistant.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MRSummaryRepository extends JpaRepository<MRSummary, Long> {
    List<MRSummary> findByProjectId(Long projectId);
}
