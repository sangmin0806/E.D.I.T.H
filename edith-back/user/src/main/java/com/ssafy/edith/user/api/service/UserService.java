package com.ssafy.edith.user.api.service;

import com.ssafy.edith.user.api.request.UserRequest;
import com.ssafy.edith.user.client.VersionControlClient;
import com.ssafy.edith.user.entity.User;
import com.ssafy.edith.user.repository.UserRepository;
import com.ssafy.edith.user.util.EncryptionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final EncryptionUtil encryptionUtil;

    private final VersionControlClient versionControlClient;

    public User signUp(UserRequest userRequest) {

        if (userRequest.vcsBaseUrl() != null && userRequest.vcsAccessToken() != null) {
            versionControlClient.validateAccessToken(userRequest.vcsBaseUrl(), userRequest.vcsAccessToken());
        }

        String encryptedPassword = passwordEncoder.encode(userRequest.password());
        String encryptedAccessToken = encryptionUtil.encrypt(userRequest.vcsAccessToken());

        User user = User.builder()
                .email(userRequest.email())
                .password(encryptedPassword)
                .vcsBaseUrl(userRequest.vcsBaseUrl())
                .vcsAccessToken(encryptedAccessToken)
                .build();

        return userRepository.save(user);
    }


}
