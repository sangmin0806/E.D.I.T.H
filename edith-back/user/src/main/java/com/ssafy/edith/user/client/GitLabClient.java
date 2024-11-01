package com.ssafy.edith.user.client;

import com.ssafy.edith.user.client.valueobject.GitLabProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class GitLabClient implements VersionControlClient{
    private final RestTemplate restTemplate;

    @Override
    public void validateAccessToken(String baseUrl, String accessToken) {
        String apiUrl = baseUrl + "/api/v4/user";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, String.class);
            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new IllegalArgumentException("유효하지 않은 GitLab Personal Access Token입니다.");
            }
        } catch (HttpClientErrorException e) {
            throw new IllegalArgumentException("유효하지 않은 GitLab Personal Access Token입니다.", e);
        }
    }

    @Override
    public GitLabProfile fetchProfile(String baseUrl, String accessToken) {
        String apiUrl = baseUrl + "/api/v4/user";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<GitLabProfile> response = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, GitLabProfile.class);
            System.out.println(response.getBody());
            return response.getBody();
        } catch (HttpClientErrorException e) {
            throw new IllegalArgumentException("유효하지 않은 GitLab Personal Access Token입니다.", e);
        }
        }

}
