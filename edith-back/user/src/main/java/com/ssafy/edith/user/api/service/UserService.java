package com.ssafy.edith.user.api.service;

import com.ssafy.edith.user.api.request.UserRequest;
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

    public User signUp(UserRequest userRequest) {

        String encryptedPassword = passwordEncoder.encode(userRequest.password());
        String encryptedAccessToken = encryptionUtil.encrypt(userRequest.gitlabPersonalAccessToken());

        User user = User.builder()
                .email(userRequest.email())
                .password(encryptedPassword)
                .vcsBaseUrl(userRequest.vcsBaseUrl())
                .gitlabPersonalAccessToken(encryptedAccessToken)
                .build();

        return userRepository.save(user);
    }
}
