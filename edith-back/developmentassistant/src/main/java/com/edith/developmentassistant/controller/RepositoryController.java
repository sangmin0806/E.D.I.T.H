package com.edith.developmentassistant.controller;

import static com.edith.developmentassistant.controller.ApiUtils.success;

import com.edith.developmentassistant.controller.ApiUtils.ApiResult;
import com.edith.developmentassistant.controller.dto.request.RegisterRepositoryRequest;
import com.edith.developmentassistant.controller.dto.response.RegisterRepositoryResponse;
import com.edith.developmentassistant.service.RepositoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/repository")
@RequiredArgsConstructor
public class RepositoryController {

    private final RepositoryService repositoryService;

    @PostMapping
    public ApiResult<RegisterRepositoryResponse> registerRepository(
            @RequestBody RegisterRepositoryRequest registerRepositoryRequest)
    {
        RegisterRepositoryResponse registerRepositoryResponse = repositoryService.registerRepository(
                registerRepositoryRequest.toRegisterRepositoryServiceRequest());
        return success(registerRepositoryResponse);
    }
}
