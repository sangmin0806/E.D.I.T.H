package com.ssafy.edith.user.api.controller;

import com.ssafy.edith.user.api.request.SignInRequest;
import com.ssafy.edith.user.api.request.SignUpRequest;
import com.ssafy.edith.user.api.response.SignInResponse;
import com.ssafy.edith.user.api.service.UserService;
import com.ssafy.edith.user.util.CookieUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static com.ssafy.edith.user.api.controller.ApiUtils.success;
import static com.ssafy.edith.user.api.controller.ApiUtils.ApiResult;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResult<Void> signUp(@RequestBody SignUpRequest signUpRequest) {
        userService.signUp(signUpRequest);
        return success(null);
    }

    @PostMapping("/sign-in")
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<SignInResponse> signIn(@RequestBody SignInRequest signInRequest, HttpServletResponse response) {
        SignInResponse signInResponse = userService.signIn(signInRequest, response);
        return success(signInResponse);
    }
}
