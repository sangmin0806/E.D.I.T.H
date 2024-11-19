package com.edith.developmentassistant.infrastructure.repository.jpa;

import com.edith.developmentassistant.domain.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {

}
