package com.edith.developmentassistant.infrastructure.client.rag;

import com.edith.developmentassistant.infrastructure.client.rag.rag.CodeReviewRequest;
import com.edith.developmentassistant.infrastructure.client.rag.rag.CodeReviewResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;


@Slf4j
@Service
public class RagServiceClient {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${api.flask.rag}")
    private String URL;

    public RagServiceClient(@Qualifier("ragRestTemplate")
                            RestTemplate restTemplate,
                            ObjectMapper objectMapper
    ) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public CodeReviewResponse commentCodeReview(CodeReviewRequest request) {
        String url = URL + "/code-review";
        log.info("Request to RAG: {}", request);

        try {
            String requestJson = objectMapper.writeValueAsString(request);
            log.info("Request to RAG (JSON): {}", requestJson);
        } catch (Exception e) {
            log.error("Failed to convert request to JSON", e);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<CodeReviewRequest> requestEntity = new HttpEntity<>(request, headers);

        try {
            log.info("Sending POST request to URL: {}", url);
            log.info("Request Headers: {}", headers);
            log.info("Request Body: {}", requestEntity.getBody());

            return restTemplate.postForObject(url, requestEntity, CodeReviewResponse.class);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.error("HTTP Error: Status code {}, Response body {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw e;
        } catch (Exception ex) {
            log.error("Error during code review request: {}", ex.getMessage(), ex);
            throw new RuntimeException("Error during code review request", ex);
        }
    }

    public String getHealthCheck() {
        String url = URL + "/health-check";
        log.info("Sending GET request to URL: {}", url);
        return restTemplate.getForObject(url, String.class);
    }

    public String sendAdviceRequest(Long projectId, String token, List<String> mrSummaries) {
        String url = URL + "/advice";

        log.info("Sending POST request to URL: {}", url);
        log.info("mrSummaries: {}", mrSummaries);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<List<String>> requestEntity = new HttpEntity<>(mrSummaries, headers);

        try {
            // JSON 응답을 문자열로 수신
            String response = restTemplate.postForObject(url, requestEntity, String.class);
            log.info("Received response: {}", response);

            // JSON 데이터 파싱
            Map<String, Object> responseData = objectMapper.readValue(response, Map.class);

            // "advice" 필드 추출
            if (responseData.containsKey("advice")) {
                String advice = (String) responseData.get("advice");
                log.info("Decoded advice: {}", advice);
                return advice; // "advice" 필드 값 반환
            } else {
                throw new RuntimeException("Response does not contain 'advice' field.");
            }
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.error("HTTP Error: Status code {}, Response body {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw e;
        } catch (Exception ex) {
            log.error("Error during advice request: {}", ex.getMessage(), ex);
            throw new RuntimeException("Error during advice request", ex);
        }
    }

}
