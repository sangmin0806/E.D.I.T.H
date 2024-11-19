package com.edith.developmentassistant.infrastructure.external.gitlab.dto;

import lombok.Getter;
import java.util.List;

@Getter
public class GitCommit {
    private String id;
    private String short_id;
    private String message;
    private String title;
    private String author_name;
    private String author_email; // 이메일 필드 추가
    private String authored_date;
    private String committer_name;
    private String committer_email;
    private String committed_date;
    private List<String> parent_ids;
}
