package com.edith.developmentassistant.service;

import com.edith.developmentassistant.client.dto.UserDto;
import com.edith.developmentassistant.client.user.UserApiClient;
import com.edith.developmentassistant.domain.MRSummary;
import com.edith.developmentassistant.domain.UserProject;
import com.edith.developmentassistant.repository.MRSummaryRepository;
import com.edith.developmentassistant.repository.PortfolioRepository;
import com.edith.developmentassistant.repository.UserProjectRepository;
import com.edith.developmentassistant.service.dto.Summary;
import com.edith.developmentassistant.service.dto.request.CreatePortfolioServiceRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final UserProjectRepository userProjectRepository;
    private final MRSummaryRepository mrSummaryRepository;
    private final UserApiClient userApiClient;

    // Portfolio 생성 로직
    public void createPortfolio(String accessToken, String projectId, String branch) {
        // 1. User 찾기
        UserDto user = userApiClient.getUserByToken(accessToken);
        // 2. project summery 찾기 -> projectId 로 찾기
        List<Summary> summaries = mrSummaryRepository.findByProjectId(Long.parseLong(projectId)).stream()
                .map(Summary::from)
                .toList();
        // 3. GitLab 에서 해당 Branch 의 MR 리스트 받아 파싱하기

        // 4. Flask 에 포폴 생성 요청하기

        // 5. 포트폴리오 받아 저장, 반환하기

    }
}
