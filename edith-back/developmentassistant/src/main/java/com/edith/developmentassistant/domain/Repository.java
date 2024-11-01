package com.edith.developmentassistant.domain;

import com.edith.developmentassistant.controller.dto.response.RegisterRepositoryResponse;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Repository extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    String url;
    String name;
    String personalAccessToken;
    String description;
    Long projectId;
    String pushEventsBranchFilter;
    boolean codeReview;

    @Builder
    private Repository(String url,
                       String name,
                       String personalAccessToken,
                       String description,
                       Long projectId,
                       String pushEventsBranchFilter,
                       boolean codeReview) {
        this.url = url;
        this.name = name;
        this.personalAccessToken = personalAccessToken;
        this.description = description;
        this.projectId = projectId;
        this.pushEventsBranchFilter = pushEventsBranchFilter;
        this.codeReview = codeReview;
    }

    public RegisterRepositoryResponse toRegisterRepositoryResponse() {
        return new RegisterRepositoryResponse(
                this.name,
                this.description,
                this.projectId,
                this.pushEventsBranchFilter,
                this.codeReview
        );
    }
}
