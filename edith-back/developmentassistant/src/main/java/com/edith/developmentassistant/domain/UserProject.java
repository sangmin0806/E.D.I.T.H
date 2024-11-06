package com.edith.developmentassistant.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserProject extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private String description;
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    private Project project;

    @Builder
    private UserProject(Long userId, String description, String title, Project project) {
        this.userId = userId;
        this.description = description;
        this.title = title;
        this.project = project;
    }
}
