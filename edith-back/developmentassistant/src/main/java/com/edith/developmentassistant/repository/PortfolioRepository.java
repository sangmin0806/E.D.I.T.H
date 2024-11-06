package com.edith.developmentassistant.repository;

import com.edith.developmentassistant.domain.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
}
