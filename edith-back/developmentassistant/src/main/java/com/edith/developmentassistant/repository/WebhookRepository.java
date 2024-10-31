package com.edith.developmentassistant.repository;

import com.edith.developmentassistant.domain.Webhook;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WebhookRepository extends JpaRepository<Webhook, Long> {
}
