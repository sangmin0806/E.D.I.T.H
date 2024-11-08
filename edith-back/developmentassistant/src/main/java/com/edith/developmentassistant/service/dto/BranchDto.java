package com.edith.developmentassistant.service.dto;

import com.edith.developmentassistant.domain.Branch;

public record BranchDto(Long id , String name) {

    public static BranchDto from(Branch branch) {
        return new BranchDto(branch.getId(), branch.getName());
    }
}
