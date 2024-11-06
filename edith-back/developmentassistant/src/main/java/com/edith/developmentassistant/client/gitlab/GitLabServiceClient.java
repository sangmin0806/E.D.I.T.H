package com.edith.developmentassistant.client.gitlab;

import com.edith.developmentassistant.client.dto.RegisterWebhookRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class GitLabServiceClient {

    private static final String GITLAB_API_URL = "https://lab.ssafy.com/api/v4";
    private static final String REGISTER_HOOK_ENDPOINT = "/projects/";

    private final RestTemplate restTemplate;

    @Autowired
    public GitLabServiceClient(
            @Qualifier("gitLabRestTemplate") RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
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
            log.info("Personal Access Token: {}", personalAccessToken);
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
