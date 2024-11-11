package com.edith.developmentassistant.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Entity
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserProject extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private String description;
    private String title;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Project project;

    @OneToOne(mappedBy = "userProject")
    private Portfolio portfolio;

    @Builder
    private UserProject(Long userId, String description, String title, Project project) {
        this.userId = userId;
        this.description = description;
        this.title = title;
        this.project = project;
    }
}
