package com.edith.developmentassistant.repository;

import com.edith.developmentassistant.domain.Branch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BranchRepository extends JpaRepository<Branch, Long> {
}
