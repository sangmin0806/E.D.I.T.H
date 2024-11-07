package com.edith.developmentassistant.controller;

import com.edith.developmentassistant.controller.dto.request.RegisterProjectRequest;
import com.edith.developmentassistant.controller.dto.response.RegisterProjectResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.edith.developmentassistant.controller.ApiUtils.success;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/portfolio")
public class PortfolioController {

    @GetMapping("/{porjectId}")
    public ApiUtils.ApiResult<RegisterProjectResponse> registerProjects(
            @CookieValue(value = "accessToken", required = false) String token) {



        return success(null);
    }
}
