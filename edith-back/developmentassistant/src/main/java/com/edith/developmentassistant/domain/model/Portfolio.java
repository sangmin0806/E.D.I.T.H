package com.edith.developmentassistant.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "portfolio")
public class Portfolio extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
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

    public void updateContent(String content) {
        this.content = content;
    }

    public void updateDates(LocalDateTime startDate, LocalDateTime endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
