package com.edith.developmentassistant.client.rag;

import com.edith.developmentassistant.client.dto.rag.CodeReviewChanges;
import com.edith.developmentassistant.client.dto.rag.CodeReviewRequest;
import com.edith.developmentassistant.client.dto.rag.CodeReviewResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
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
            log.info("Sending POST request to URL: {}", URL);
            log.info("Request Headers: {}", headers);
            log.info("Request Body: {}", requestEntity.getBody());

            return restTemplate.postForObject(URL, requestEntity, CodeReviewResponse.class);
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
}
