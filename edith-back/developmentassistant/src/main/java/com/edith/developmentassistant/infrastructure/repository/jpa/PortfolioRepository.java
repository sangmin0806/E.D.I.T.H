package com.edith.developmentassistant.infrastructure.repository.jpa;

import com.edith.developmentassistant.application.dto.response.FindAllPortfolioResponse;
import com.edith.developmentassistant.domain.model.Portfolio;
import com.edith.developmentassistant.domain.model.UserProject;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
    @Query("SELECT new com.edith.developmentassistant.application.dto.response.FindAllPortfolioResponse(" +
            "up.title, " +
            "up.description, " +
            "p.lastModifiedDate, " +
            "up.project.id) " +
            "FROM Portfolio p " +
            "JOIN p.userProject up " +
            "WHERE up.userId = :userId")
    List<FindAllPortfolioResponse> findAllDtoByUserId(@Param("userId") Long userId);

    Optional<Portfolio> findByUserProject(UserProject userProject);

}
