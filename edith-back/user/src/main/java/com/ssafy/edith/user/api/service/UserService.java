package com.ssafy.edith.user.api.service;

import com.ssafy.edith.user.api.request.SignInRequest;
import com.ssafy.edith.user.api.request.SignUpRequest;
import com.ssafy.edith.user.api.response.SignInResponse;
import com.ssafy.edith.user.client.VersionControlClient;
import com.ssafy.edith.user.entity.User;
import com.ssafy.edith.user.jwt.JwtUtil;
import com.ssafy.edith.user.repository.UserRepository;
import com.ssafy.edith.user.util.CookieUtil;
import com.ssafy.edith.user.util.EncryptionUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final EncryptionUtil encryptionUtil;
    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;

    private final VersionControlClient versionControlClient;

    public User signUp(SignUpRequest signUpRequest) {

        if (hasVcsInfo(signUpRequest)) {
            versionControlClient.validateAccessToken(signUpRequest.vcsBaseUrl(), signUpRequest.vcsAccessToken());
        }

        String encryptedPassword = passwordEncoder.encode(signUpRequest.password());
        String encryptedAccessToken = encryptionUtil.encrypt(signUpRequest.vcsAccessToken());

        User user = signUpRequest.toEntity(encryptedPassword, encryptedAccessToken);

        return userRepository.save(user);
    }
    public SignInResponse signIn(SignInRequest signInRequest, HttpServletResponse response) {
        User user = validateUserCredentials(signInRequest);

        String accessToken = jwtUtil.createJwtToken(user.getId(), user.getEmail());

        cookieUtil.addTokenToCookie(response,accessToken);

        return new SignInResponse(user.getId(), user.getEmail(), accessToken);
    }

    private User validateUserCredentials(SignInRequest signInRequest) {
        User user = userRepository.findByEmail(signInRequest.email())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일입니다."));

        if (!passwordEncoder.matches(signInRequest.password(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        return user;
    }

    private boolean hasVcsInfo(SignUpRequest signUpRequest) {
        return signUpRequest.vcsBaseUrl() != null && signUpRequest.vcsAccessToken() != null;
    }

}
