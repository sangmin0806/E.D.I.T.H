package com.edith.developmentassistant.client.user;

import com.edith.developmentassistant.client.dto.UserDto;
import com.edith.developmentassistant.controller.ApiUtils.ApiResult;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class UserServiceClient {

    private static final String USER_API_URL = "http://localhost:8081";
    private static final String USER_INFO_ENDPOINT = "/api/v1/users/info";

    private final RestTemplate userServiceRestTemplate;

    public UserServiceClient(
            @Qualifier("userServiceRestTemplate")
            RestTemplate userServiceRestTemplate) {
        this.userServiceRestTemplate = userServiceRestTemplate;
    }

    public UserDto getUserByToken(String accessToken) {
        String url = USER_API_URL + USER_INFO_ENDPOINT;

        String cookie = "accessToken=" + accessToken;

        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("Cookie", cookie);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        try {
            log.info("Fetching user info with access token.");
            ResponseEntity<ApiResult<UserDto>> response = userServiceRestTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    requestEntity,
                    new ParameterizedTypeReference<ApiResult<UserDto>>() {
                    }
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                ApiResult<UserDto> apiResult = response.getBody();
                if (apiResult.isSuccess()) {
                    log.info("User info fetched successfully.");
                    return apiResult.getResponse();
                } else if (apiResult.getError() != null) {
                    log.error("API 호출 실패: {}", apiResult.getError().getMessage());
                    throw new RuntimeException("API 호출 실패: " + apiResult.getError().getMessage());
                } else {
                    log.error("API 응답이 null입니다.");
                    throw new RuntimeException("API 응답이 null입니다.");
                }
            } else {
                log.error("Failed to fetch user info. Status Code: {}", response.getStatusCode());
                throw new RuntimeException("Failed to fetch user info. Status Code: " + response.getStatusCode());
            }
        } catch (Exception ex) {
            log.error("Error fetching user info: {}", ex.getMessage(), ex);
            throw new RuntimeException("Error fetching user info", ex);
        }
    }
}
