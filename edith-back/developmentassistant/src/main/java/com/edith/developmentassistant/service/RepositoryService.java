package com.edith.developmentassistant.service;

import com.edith.developmentassistant.controller.dto.request.RegisterRepositoryRequest;
import com.edith.developmentassistant.controller.dto.response.RegisterRepositoryResponse;
import com.edith.developmentassistant.domain.Repository;
import com.edith.developmentassistant.repository.RepositoryRepository;
import com.edith.developmentassistant.service.dto.request.RegisterRepositoryServiceRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RepositoryService {

    private final WebhookService webhookService;
    private final RepositoryRepository repositoryRepository;

    public RegisterRepositoryResponse registerRepository(RegisterRepositoryServiceRequest request) {
        try {
            webhookService.registerWebhook(request);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to register webhook", e);
        }
        Repository save = repositoryRepository.save(request.toRepository());
        return save.toRegisterRepositoryResponse();
    }
}
