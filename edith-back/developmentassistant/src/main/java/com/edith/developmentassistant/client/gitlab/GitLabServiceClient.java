package com.edith.developmentassistant.client.gitlab;

import com.edith.developmentassistant.client.dto.CommentRequest;
import com.edith.developmentassistant.client.dto.RegisterWebhookRequest;
import com.edith.developmentassistant.client.dto.mergerequest.MergeRequestDiffResponse;
import com.edith.developmentassistant.client.dto.rag.CodeReviewRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
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
        ObjectNode requestBody = objectMapper.createObjectNode();
        requestBody.put("name", TOKEN_NAME);
        requestBody.putArray("scopes").addAll(Arrays.asList(
                objectMapper.convertValue("api", JsonNode.class),
                objectMapper.convertValue("read_api", JsonNode.class),
                objectMapper.convertValue("read_repository", JsonNode.class),
                objectMapper.convertValue("write_repository", JsonNode.class),
                objectMapper.convertValue("read_registry", JsonNode.class),
                objectMapper.convertValue("write_registry", JsonNode.class)
        ));

        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody.toString(), headers);

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
