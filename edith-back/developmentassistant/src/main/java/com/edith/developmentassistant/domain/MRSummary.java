package com.edith.developmentassistant.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "mr_summary")
public class MRSummary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "mr_id")
    private String mrId;

    @Column(name = "gitlab_email")
    private String gitlabEmail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    private String content;

    @Builder
    private MRSummary(String mrId, String gitlabEmail, Project project, String content) {
        this.mrId = mrId;
        this.gitlabEmail = gitlabEmail;
        this.project = project;
        this.content = content;
    }
}
