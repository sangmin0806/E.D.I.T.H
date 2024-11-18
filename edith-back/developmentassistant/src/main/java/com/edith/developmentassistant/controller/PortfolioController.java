package com.edith.developmentassistant.controller;


import static com.edith.developmentassistant.controller.ApiUtils.success;

import com.edith.developmentassistant.service.PortfolioService;
import com.edith.developmentassistant.service.dto.PortfolioDto;
import com.edith.developmentassistant.service.dto.response.FindAllPortfolioResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/portfolio")
public class PortfolioController {

    private final PortfolioService portfolioService;

    @PutMapping("/{projectId}")
    public ApiUtils.ApiResult<?> makePortfolio(
            @CookieValue(value = "accessToken", required = false) String token,
            @PathVariable String projectId
    ) {
        // TODO : branch가 우리 서비스에서 관리가 안되서 임시처리로 develop 이라고 함
        // TODO : @RequestParam String branch

        String branch = "develop";
        PortfolioDto result = portfolioService.createPortfolio(token, projectId, branch);
        return success(result);
    }

    @PostMapping("/{projectId}")
    public ApiUtils.ApiResult<?> updatePortfolio(
            @CookieValue(value = "accessToken", required = false) String token,
            @RequestBody PortfolioDto createPortfolioResponse) {

        PortfolioDto result = portfolioService.savePortfolio(token, createPortfolioResponse);
        return success(result);
    }

    @GetMapping()
    public ApiUtils.ApiResult<List<FindAllPortfolioResponse>> getPortfolios(
            @CookieValue(value = "accessToken", required = false) String token) {

        return success(portfolioService.findAllPortfolioResponseList(token));
    }

    @GetMapping("/{projectId}")
    public ApiUtils.ApiResult<PortfolioDto> getPortfolios(
            @CookieValue(value = "accessToken", required = false) String token,
            @PathVariable String projectId) {
        return success(portfolioService.getPortfolio(token, projectId));
    }

    @GetMapping("/health-check")
    public ApiUtils.ApiResult<String> healthCheck() {
        return success("I'm alive");
    }

}
