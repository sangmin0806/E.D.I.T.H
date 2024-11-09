package com.edith.developmentassistant.client.gitlab;

import com.edith.developmentassistant.client.dto.CommentRequest;
import com.edith.developmentassistant.client.dto.ProjectAccessTokenRequest;
import com.edith.developmentassistant.client.dto.RegisterWebhookRequest;

import com.edith.developmentassistant.client.dto.gitlab.GitBranch;
import com.edith.developmentassistant.client.dto.gitlab.GitMerge;

import com.edith.developmentassistant.client.dto.gitlab.ContributorDto;

import com.edith.developmentassistant.client.dto.mergerequest.MergeRequestDiffResponse;
import com.edith.developmentassistant.client.dto.gitlab.GitCommit;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class GitLabServiceClient {

    private static final String GITLAB_API_URL = "https://lab.ssafy.com/api/v4";
    private static final String REGISTER_HOOK_ENDPOINT = "/projects/";
    private static final String MR_DIFF_ENDPOINT = "/projects/%d/merge_requests/%d/changes"; // DIFF 엔드포인트
    private static final String TOKEN_NAME = "E.D.I.T.H";


    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public GitLabServiceClient(
            @Qualifier("gitLabRestTemplate")
            RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public void registerWebhook(Long projectId, String personalAccessToken) {
        String url = GITLAB_API_URL + REGISTER_HOOK_ENDPOINT + projectId + "/hooks";

        RegisterWebhookRequest requestBody = createRequestBody();

        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("PRIVATE-TOKEN", personalAccessToken);

        HttpEntity<RegisterWebhookRequest> requestEntity = new HttpEntity<>(requestBody, headers);

        try {
            log.info("Registering webhook for project ID: {}", projectId);
            log.info("Request URL: {}", url);
            log.info("Request Body: {}", requestBody);
            log.info("Request Headers: {}", headers);
            ResponseEntity<Void> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    requestEntity,
                    Void.class,
                    projectId
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("Webhook registered successfully.");

            } else {
                log.error("Failed to register webhook. Status Code: {}", response.getStatusCode());
                throw new RuntimeException("Failed to register webhook");
            }
        } catch (Exception ex) {
            log.error("Error registering webhook: {}", ex.getMessage(), ex);
            throw ex;
        }
    }

    public MergeRequestDiffResponse fetchMergeRequestDiff(Long projectId, Long mergeRequestIid,
                                                          String personalAccessToken) {
        String url = String.format(GITLAB_API_URL + MR_DIFF_ENDPOINT, projectId, mergeRequestIid);

        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("PRIVATE-TOKEN", personalAccessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        try {
            log.info("Fetching MR DIFF for project ID: {} and MR IID: {}", projectId, mergeRequestIid);
            log.info("Request URL: {}", url);

            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    requestEntity,
                    String.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("Fetched MR DIFF successfully.");
                log.info("Response Body: {}", response.getBody());
                return objectMapper.readValue(response.getBody(), MergeRequestDiffResponse.class);
            } else {
                log.error("Failed to fetch MR DIFF. Status Code: {}", response.getStatusCode());
                throw new RuntimeException("Failed to fetch MR DIFF");
            }
        } catch (Exception ex) {
            log.error("Error fetching MR DIFF: {}", ex.getMessage(), ex);
            throw new RuntimeException("Error fetching MR DIFF");
        }
    }

    public void addMergeRequestComment(Long projectId, Long mergeRequestIid, String token, String review) {
        String url = GITLAB_API_URL + "/projects/" + projectId + "/merge_requests/" + mergeRequestIid + "/notes";

        // 리뷰 내용 설정
        CommentRequest commentRequest = new CommentRequest(review);

        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("PRIVATE-TOKEN", token);

        HttpEntity<CommentRequest> requestEntity = new HttpEntity<>(commentRequest, headers);

        try {
            log.info("Adding comment to MR ID {} in project {}: {}", mergeRequestIid, projectId, review);
            ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Void.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("Comment added successfully to MR.");
            } else {
                log.error("Failed to add comment. Status code: {}", response.getStatusCode());
            }
        } catch (Exception ex) {
            log.error("Error adding comment to MR: {}", ex.getMessage(), ex);
        }
    }

    public String generateProjectAccessToken(Long projectId, String personalAccessToken) {
        String url = GITLAB_API_URL + "/projects/" + projectId + "/access_tokens";

        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("PRIVATE-TOKEN", personalAccessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 요청 본문 생성 (이름 및 모든 권한 설정 포함)

        log.info("Generating access token for project ID: {}", projectId);
        log.info("Request URL: {}", url);
        log.info("Request Headers: {}", headers);

        ProjectAccessTokenRequest projectAccessTokenRequest = ProjectAccessTokenRequest.builder()
                .name("E.D.I.T.H")
                .scopes(Arrays.asList(
                        "api",
                        "read_api",
                        "write_repository",
                        "read_repository"
                ))
                .expiresAt(LocalDate.now().plusYears(1)) // 현재 날짜로부터 1년 후
                .accessLevel(40)
                .build();

        log.info("Request Body: {}", projectAccessTokenRequest);
        try {
            log.info("Request Body: {}", objectMapper.writeValueAsString(projectAccessTokenRequest));
        } catch (JsonProcessingException e) {
            log.error("Error generating access token: {}", e.getMessage(), e);
        }

        HttpEntity<ProjectAccessTokenRequest> requestEntity = new HttpEntity<>(projectAccessTokenRequest, headers);

        try {
            log.info("Generating access token for project ID: {}", projectId);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

            if (response.getStatusCode() == HttpStatus.CREATED) {
                log.info("Access token generated successfully.");

                // JSON 파싱하여 access_token 추출
                JsonNode responseBody = objectMapper.readTree(response.getBody());
                String accessToken = responseBody.path("token").asText();

                log.info("Access token: {}", accessToken);
                return accessToken;
            } else {
                log.error("Failed to generate access token. Status code: {}", response.getStatusCode());
            }
        } catch (Exception ex) {
            log.error("Error generating access token: {}", ex.getMessage(), ex);
        }

        return null; // 실패 시 null 반환
    }


    public List<GitCommit> fetchCommitsInMergeRequest(Long projectId, Long mergeRequestIid, String projectAccessToken) {
        String url = GITLAB_API_URL + "/projects/" + projectId + "/merge_requests/" + mergeRequestIid + "/commits";

        HttpHeaders headers = new HttpHeaders();
        headers.set("PRIVATE-TOKEN", projectAccessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<List<GitCommit>> response = restTemplate.exchange(
                    url, HttpMethod.GET, entity, new ParameterizedTypeReference<List<GitCommit>>() {
                    });

            return response.getBody();
        } catch (RestClientException e) {
            log.error("Error fetching commits for merge request {} in project {}: {}", mergeRequestIid, projectId, e.getMessage());
            throw e;
        }
    }

    public GitCommit fetchCommitDetails(Long projectId, String commitSha, String projectAccessToken) {
        String url = GITLAB_API_URL + "/projects/" + projectId + "/repository/commits/" + commitSha;
        HttpHeaders headers = new HttpHeaders();
        headers.set("PRIVATE-TOKEN", projectAccessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<GitCommit> response = restTemplate.exchange(
                    url, HttpMethod.GET, entity, GitCommit.class
            );

            return response.getBody();
        } catch (RestClientException e) {
            log.error("Error fetching GitLab commit details for commit {} in project {}: {}", commitSha, projectId, e.getMessage());
        }
    }

    public List<ContributorDto> fetchContributors(Long projectId, String personalAccessToken) {
        String url = GITLAB_API_URL + "/projects/" + projectId + "/members";

        HttpHeaders headers = new HttpHeaders();
        headers.set("PRIVATE-TOKEN", personalAccessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<List<ContributorDto>> response = restTemplate.exchange(
                    url, HttpMethod.GET, entity, new ParameterizedTypeReference<List<ContributorDto>>() {
                    });

            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("Contributors fetched successfully for project ID: {}", projectId);
                try {
                    // response.getBody()를 JSON 문자열로 변환
                    String responseBodyJson = objectMapper.writeValueAsString(response.getBody());
                    log.info("Response Body: {}", responseBodyJson);
                } catch (JsonProcessingException e) {
                    log.error("Failed to convert response body to JSON", e);
                }
                return response.getBody();
            } else {
                log.error("Failed to fetch contributors. Status Code: {}", response.getStatusCode());
                throw new RuntimeException("Failed to fetch contributors");
            }
        } catch (RestClientException e) {
            log.error("Error fetching contributors for project {}: {}", projectId, e.getMessage());

            throw e;
        }
    }

    public List<GitMerge> fetchGitLabMergeRequests(Long projectId, String projectAccessToken) {
        String url = GITLAB_API_URL + "/projects/" + projectId + "/merge_requests?state=merged&per_page=5";

        HttpHeaders headers = new HttpHeaders();
        headers.set("PRIVATE-TOKEN", projectAccessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);


        try {
            ResponseEntity<List<GitMerge>> response = restTemplate.exchange(
                    url, HttpMethod.GET, entity, new ParameterizedTypeReference<List<GitMerge>>() {});
            return response.getBody();
        } catch (RestClientException e) {
            log.error("Error fetching GitLab merge for project {}: {}", projectId, e.getMessage());
            throw e;
        }
    }

    private RegisterWebhookRequest createRequestBody() {
        return RegisterWebhookRequest.builder()
                .url("http://k11c206.p.ssafy.io:8082/webhook")
                .description("Development Assistant Webhook")
                .pushEvents(true)
                .tagPushEvents(true)
                .mergeRequestsEvents(true)
                .enableSslVerification(false)
                .pushEventsBranchFilter(null)
                .build();
    }
}
