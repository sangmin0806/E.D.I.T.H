package com.edith.developmentassistant.client.rag;

import com.edith.developmentassistant.client.dto.rag.CodeReviewChanges;
import com.edith.developmentassistant.client.dto.rag.CodeReviewRequest;
import com.edith.developmentassistant.client.dto.rag.CodeReviewResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class RagServiceClient {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final String URL = "http://k11c206.p.ssafy.io:8083/api/v1/rag";

    public RagServiceClient(@Qualifier("ragRestTemplate")
                            RestTemplate restTemplate,
                            ObjectMapper objectMapper
    ) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public CodeReviewResponse commentCodeReview(CodeReviewRequest request) {
        String url = URL + "/code-review";

        try {
            // 객체를 JSON 문자열로 변환하여 로그 출력
            String requestJson = objectMapper.writeValueAsString(request);
            log.info("Request to RAG (JSON): {}", requestJson);
        } catch (Exception e) {
            log.error("Failed to convert request to JSON", e);
        }

        HttpEntity<CodeReviewRequest> requestEntity = new HttpEntity<>(request);

//        log.info("Request to RAG: {}", requestEntity);

        return restTemplate.postForObject(url, requestEntity, CodeReviewResponse.class);
    }
}
