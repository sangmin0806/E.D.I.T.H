package com.edith.developmentassistant.application.dto;

import com.edith.developmentassistant.domain.model.Branch;

public record BranchDto(Long id , String name) {

    public static BranchDto from(Branch branch) {
        return new BranchDto(branch.getId(), branch.getName());
    }
}
