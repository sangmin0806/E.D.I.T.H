package com.ssafy.edith.user.client;

import com.ssafy.edith.user.client.valueobject.FaceEmbeddingRegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class FastAPIClient {
    private final RestTemplate restTemplate;
    @Value("${fastapi.url}")
    private String fastApiUrl;

    public ResponseEntity<String> registerFaceEmbedding(FaceEmbeddingRegisterRequest faceEmbeddingRegisterRequest) {
        String url = fastApiUrl + "api/v1/face-recognition/register-face";

        try {
            return restTemplate.postForEntity(url, faceEmbeddingRegisterRequest, String.class);
        } catch (RestClientException e) {
            throw new RestClientException("FastAPI 호출 중 오류 발생: " + e.getMessage(), e);
        }

    }
}
