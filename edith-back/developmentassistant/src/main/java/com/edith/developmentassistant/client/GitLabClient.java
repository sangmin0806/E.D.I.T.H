package com.edith.developmentassistant.client;

import com.edith.developmentassistant.client.dto.RegisterWebhookRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

@Service
@RequiredArgsConstructor
@Slf4j
public class GitLabClient {

    private final RestTemplate restTemplate;

    public void registerWebhook(String branch, Integer projectId, String personalAccessToken) {
        String url = "https://lab.ssafy.com/api/v4/projects/824085/hooks";

        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("PRIVATE-TOKEN", personalAccessToken);

        // 요청 본문 생성
        RegisterWebhookRequest requestBody = createRequestBody(branch);

        // HttpEntity로 헤더와 본문을 함께 설정
        HttpEntity<RegisterWebhookRequest> requestEntity = new HttpEntity<>(requestBody, headers);

        try {
            log.info("Sending POST request to GitLab API: {}", url);
            log.debug("Request Headers: {}", headers);
            log.debug("Request Body: {}", requestBody);

            // POST 요청 보내기
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
            String response = responseEntity.getBody();

            log.info("Webhook registered successfully: {}", response);
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            // HTTP 오류 발생 시 상세 로그 출력
            log.error("Error registering webhook: Status Code: {}, Response Body: {}", ex.getStatusCode(), ex.getResponseBodyAsString());
            throw ex;
        } catch (Exception ex) {
            // 기타 예외 발생 시 로그 출력
            log.error("Unexpected error while registering webhook: {}", ex.getMessage(), ex);
            throw ex;
        }
    }

    private RegisterWebhookRequest createRequestBody(String branch) {
        return RegisterWebhookRequest.builder()
                .url("http://k11c206.p.ssafy.io:8082/webhook")
                .description("Development Assistant Webhook")
                .pushEvents(true)
                .tagPushEvents(true)
                .mergeRequestsEvents(true)
                .enableSslVerification(false)
                .pushEventsBranchFilter(branch)
                .build();
    }
}
