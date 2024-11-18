package com.edith.developmentassistant.repository;

import com.edith.developmentassistant.domain.Project;
import com.edith.developmentassistant.domain.UserProject;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {

}
