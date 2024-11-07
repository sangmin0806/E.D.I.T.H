package com.edith.developmentassistant.controller;

import com.edith.developmentassistant.controller.dto.response.RegisterProjectResponse;
import com.edith.developmentassistant.service.PortfolioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import static com.edith.developmentassistant.controller.ApiUtils.success;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/portfolio")
public class PortfolioController {

    private final PortfolioService portfolioService;

    @GetMapping("/{projectId}")
//    public ApiUtils.ApiResult<RegisterProjectResponse> registerProjects(
    public ApiUtils.ApiResult<?> registerProjects(
            @CookieValue(value = "accessToken", required = false) String token,
            @PathVariable String projectId,
            @RequestParam String branch) {

        String result = portfolioService.createPortfolio(token, projectId, branch);


        return success(result);
    }


}
