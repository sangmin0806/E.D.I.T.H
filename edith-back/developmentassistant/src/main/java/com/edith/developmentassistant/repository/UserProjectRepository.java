package com.edith.developmentassistant.repository;

import com.edith.developmentassistant.domain.UserProject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProjectRepository extends JpaRepository<UserProject, Long> {
}
