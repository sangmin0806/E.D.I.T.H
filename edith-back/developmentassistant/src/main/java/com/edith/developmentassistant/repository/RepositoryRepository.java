package com.edith.developmentassistant.repository;

import com.edith.developmentassistant.domain.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositoryRepository extends JpaRepository<Repository, Long> {
}
