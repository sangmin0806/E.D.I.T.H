package com.edith.developmentassistant.repository;

import com.edith.developmentassistant.domain.Portfolio;
import com.edith.developmentassistant.domain.UserProject;
import com.edith.developmentassistant.service.dto.response.FindAllPortfolioResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
    @Query("SELECT new com.edith.developmentassistant.service.dto.response.FindAllPortfolioResponse(" +
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
