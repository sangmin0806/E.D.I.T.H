package com.edith.developmentassistant.controller;


import static com.edith.developmentassistant.controller.ApiUtils.success;

import com.edith.developmentassistant.service.PortfolioService;
import com.edith.developmentassistant.service.dto.response.CreatePortfolioResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/portfolio")
public class PortfolioController {

    private final PortfolioService portfolioService;

    @PutMapping("/{projectId}")
//    public ApiUtils.ApiResult<RegisterProjectResponse> registerProjects(
    public ApiUtils.ApiResult<?> makePortfolio(
            @CookieValue(value = "accessToken", required = false) String token,
            @PathVariable String projectId,
            @RequestParam(required = false) String branch) {

        CreatePortfolioResponse result = portfolioService.createPortfolio(token, projectId, branch);
        return success(result);
    }


}
