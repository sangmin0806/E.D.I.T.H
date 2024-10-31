package com.ssafy.edith.user.api.service;

import com.ssafy.edith.user.api.request.UserRequest;
import com.ssafy.edith.user.client.VersionControlClient;
import com.ssafy.edith.user.entity.User;
import com.ssafy.edith.user.repository.UserRepository;
import com.ssafy.edith.user.util.EncryptionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final EncryptionUtil encryptionUtil;

    private final VersionControlClient versionControlClient;

    public User signUp(UserRequest userRequest) {

        if (hasVcsInfo(userRequest)) {
            versionControlClient.validateAccessToken(userRequest.vcsBaseUrl(), userRequest.vcsAccessToken());
        }

        String encryptedPassword = passwordEncoder.encode(userRequest.password());
        String encryptedAccessToken = encryptionUtil.encrypt(userRequest.vcsAccessToken());

        User user = userRequest.toEntity(encryptedPassword, encryptedAccessToken);

        return userRepository.save(user);
    }
    private boolean hasVcsInfo(UserRequest userRequest) {
        return userRequest.vcsBaseUrl() != null && userRequest.vcsAccessToken() != null;
    }

}
