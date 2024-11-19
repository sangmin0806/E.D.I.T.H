package com.edith.developmentassistant.infrastructure.repository.jpa;

import com.edith.developmentassistant.domain.model.Branch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BranchRepository extends JpaRepository<Branch, Long> {
}
