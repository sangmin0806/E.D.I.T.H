package com.edith.developmentassistant.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "portfolio")
public class Portfolio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_project_id")
    private UserProject userProject;

    @Lob
    private String content;

    @Column(name = "start_date")
    private LocalDateTime startDate;
    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Builder
    public Portfolio(UserProject userProject, String content, LocalDateTime startDate, LocalDateTime endDate) {
        this.userProject = userProject;
        this.content = content;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
