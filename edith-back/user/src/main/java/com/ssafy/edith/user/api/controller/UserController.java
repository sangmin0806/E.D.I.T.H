package com.ssafy.edith.user.api.controller;

import com.ssafy.edith.user.api.request.UserRequest;
import com.ssafy.edith.user.api.service.UserService;
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
    public ApiResult<Void> signUp(@RequestBody UserRequest userRequest) {
        userService.signUp(userRequest);
        return success(null);
    }

    @PostMapping("/sign-in")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResult<Void> signIn(@RequestBody UserRequest userRequest) {
        userService.signUp(userRequest);
        return success(null);
    }
}
