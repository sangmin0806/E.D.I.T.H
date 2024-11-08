package com.edith.developmentassistant.controller;


import static com.edith.developmentassistant.controller.ApiUtils.success;

import com.edith.developmentassistant.service.PortfolioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/portfolio")
public class PortfolioController {

    private final PortfolioService portfolioService;

    @PostMapping("/{projectId}")
//    public ApiUtils.ApiResult<RegisterProjectResponse> registerProjects(
    public ApiUtils.ApiResult<?> makePortfolio(
            @CookieValue(value = "accessToken", required = false) String token,
            @PathVariable String projectId,
            @RequestParam String branch) {

        String result = portfolioService.createPortfolio(token, projectId, branch);


        return success(result);
    }


}
