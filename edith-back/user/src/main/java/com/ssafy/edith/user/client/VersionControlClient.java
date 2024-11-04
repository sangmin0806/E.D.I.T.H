package com.ssafy.edith.user.client;

import com.ssafy.edith.user.client.valueobject.GitLabProfile;

public interface VersionControlClient {
    void validateAccessToken(String baseUrl, String accessToken);
    GitLabProfile fetchProfile(String baseUrl, String accessToken);
}
