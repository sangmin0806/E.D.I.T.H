package com.edith.developmentassistant.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Webhook extends BaseEntity{

    @Id
    private Integer id;

    private Integer projectId;
    private String url;
    private boolean mergeRequestsEvents;
    private String pushEventsBranchFilter;
}
