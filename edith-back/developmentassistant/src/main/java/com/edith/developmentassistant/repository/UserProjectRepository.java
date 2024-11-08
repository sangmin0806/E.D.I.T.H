package com.edith.developmentassistant.repository;

import com.edith.developmentassistant.domain.UserProject;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserProjectRepository extends JpaRepository<UserProject, Long> {

    List<UserProject> findByUserId(Long userId);

    Optional<UserProject> findByUserIdAndProjectId(Long userId, Long projectId);


}
