package com.edith.developmentassistant.repository;

import com.edith.developmentassistant.domain.UserProject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserProjectRepository extends JpaRepository<UserProject, Long> {
     Optional<UserProject> findByUserIdAndProjectId(Long userId, Long projectId);
}
