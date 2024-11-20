package com.edith.developmentassistant.infrastructure.external.gitlab.dto.mergerequest;

import com.edith.developmentassistant.infrastructure.client.rag.rag.CodeReviewChanges;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Change {
    private String diff;
    private String newPath;
    private String oldPath;
    private String aMode;
    private String bMode;
    private boolean newFile;
    private boolean renamedFile;
    private boolean deletedFile;
    private boolean generatedFile;


    public CodeReviewChanges toCodeReviewChanges() {
        return CodeReviewChanges.builder()
                .path(newPath)
                .diff(diff)
                .build();
    }
}
