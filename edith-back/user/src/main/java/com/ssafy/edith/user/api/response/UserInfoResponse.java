package com.ssafy.edith.user.api.response;

import com.ssafy.edith.user.entity.User;
import com.ssafy.edith.user.util.EncryptionUtil;

public record UserInfoResponse(String email,
                               String password,
                               boolean vcs,
                               String vcsBaseUrl,
                               String vcsAccessToken) {
    public static UserInfoResponse of(User user) {
        String decryptedAccessToken = EncryptionUtil.decrypt(user.getVcsAccessToken());

        return new UserInfoResponse(
                user.getEmail(),
                user.getPassword(),
                user.isVcs(),
                user.getVcsBaseUrl(),
                decryptedAccessToken
        );
    }
}