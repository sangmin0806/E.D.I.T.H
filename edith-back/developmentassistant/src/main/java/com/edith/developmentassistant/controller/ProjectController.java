package com.edith.developmentassistant.controller;

import static com.edith.developmentassistant.controller.ApiUtils.success;

import com.edith.developmentassistant.client.gitlab.GitLabServiceClient;
import com.edith.developmentassistant.controller.ApiUtils.ApiResult;
import com.edith.developmentassistant.controller.dto.request.RegisterProjectRequest;
import com.edith.developmentassistant.controller.dto.response.RegisterProjectResponse;
import com.edith.developmentassistant.client.dto.gitlab.GitCommit;
import com.edith.developmentassistant.controller.dto.response.gitlab.GitLabCommitsResponse;
import com.edith.developmentassistant.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    private final GitLabServiceClient gitlabServiceClient;

    @PostMapping
    public ApiResult<RegisterProjectResponse> registerProjects(
            @CookieValue(value = "accessToken", required = false) String token,
            @RequestBody RegisterProjectRequest registerProjectRequest) {
        projectService.registerProject(registerProjectRequest.toServiceRequest(), token);
        return success(null);
    }

    @GetMapping("/commits/{projectId}")
    public ApiResult<GitLabCommitsResponse> getGitLabCommits(
            @PathVariable Long projectId,
            @CookieValue(value = "accessToken", required = false) String token) {

        List<GitCommit> commits = projectService.fetchGitLabCommits(projectId, token);

        return success(GitLabCommitsResponse.of(commits));
    }

    @GetMapping
    public String healthCheck() {
        return "health check";
    }

    @PostMapping("/test")
    public void test() {
        String review = """
                <h3>통합 리뷰: <code>PortfolioController.java</code></h3>
                <h4>1. 기능 설명</h4>
                <p><code>PortfolioController</code> 클래스는 Spring Framework를 사용하여 웹 애플리케이션의 포트폴리오 관련 API를 처리하는 컨트롤러입니다. 이 클래스는 다음과 같은 기능을 수행합니다:</p>
                
                <ul>
                    <li><strong>패키지 및 의존성</strong>: <code>com.edith.developmentassistant.controller</code> 패키지에 위치하며, <code>RegisterProjectRequest</code>와 <code>RegisterProjectResponse</code> DTO를 사용하여 요청 및 응답을 처리합니다. Lombok의 <code>@RequiredArgsConstructor</code>와 <code>@Slf4j</code> 어노테이션을 사용하여 의존성 주입과 로깅을 간편하게 처리합니다.</li>
                    <li><strong>API 엔드포인트</strong>: <code>@RequestMapping("/portfolio")</code>로 시작하는 URL 경로에 대한 요청을 처리합니다. 현재 구현된 메서드는 <code>@GetMapping("/{projectId}")</code>로, 특정 프로젝트 ID에 대한 GET 요청을 처리합니다.</li>
                    <li><strong>토큰 처리</strong>: <code>@CookieValue</code>를 사용하여 클라이언트의 쿠키에서 <code>accessToken</code> 값을 읽어옵니다. 이 값은 인증이나 권한 부여에 사용될 수 있습니다.</li>
                    <li><strong>응답 처리</strong>: <code>success(null)</code>을 호출하여 성공적인 응답을 반환합니다. 현재는 실제 프로젝트 등록 로직이 구현되어 있지 않으며, <code>null</code>을 반환하고 있습니다.</li>
                </ul>
                
                <h4>2. 개선해야 할 사항</h4>
                <ul>
                    <li><strong>메서드 이름</strong>: <code>registerProjects</code>라는 메서드 이름은 GET 요청에 적합하지 않습니다. 일반적으로 GET 요청은 리소스를 조회하는 데 사용되므로, 메서드 이름을 <code>getProject</code> 또는 <code>retrieveProject</code>와 같이 변경하는 것이 좋습니다.</li>
                    <li><strong>HTTP 메서드</strong>: 프로젝트 등록은 일반적으로 POST 요청을 통해 수행됩니다. 현재 GET 요청으로 구현되어 있으므로, 이 부분을 검토하여 적절한 HTTP 메서드를 사용해야 합니다.</li>
                    <li><strong>응답 처리</strong>: 현재는 <code>success(null)</code>을 반환하고 있지만, 실제로 프로젝트 정보를 반환해야 할 필요가 있습니다. 프로젝트 ID에 따라 적절한 프로젝트 정보를 조회하고 반환하는 로직이 필요합니다.</li>
                </ul>
                
                <h4>3. 수정 필요 항목</h4>
                <ul>
                    <li><strong>버그 가능성</strong>: <code>@GetMapping("/{porjectId}")</code>에서 <code>porjectId</code>의 철자가 잘못되었습니다. <code>projectId</code>로 수정해야 합니다. 이로 인해 URL 매핑이 제대로 작동하지 않을 수 있습니다.</li>
                    <li><strong>응답 타입</strong>: <code>ApiUtils.ApiResult&lt;RegisterProjectResponse&gt;</code>를 반환하는데, 실제로는 <code>RegisterProjectResponse</code> 객체를 생성하고 반환하는 로직이 필요합니다. 현재는 <code>null</code>을 반환하고 있어, 클라이언트가 유용한 정보를 받을 수 없습니다.</li>
                    <li><strong>예외 처리</strong>: 프로젝트 ID가 유효하지 않거나, 해당 프로젝트가 존재하지 않을 경우에 대한 예외 처리가 필요합니다. 이를 통해 API의 안정성을 높일 수 있습니다.</li>
                </ul>
                
                <h4>결론</h4>
                <p>현재 <code>PortfolioController</code>는 기본적인 구조를 갖추고 있지만, 메서드 이름, HTTP 메서드, 응답 처리 및 예외 처리와 같은 여러 부분에서 개선이 필요합니다. 이러한 사항들을 반영하여 코드를 수정하면, 더 나은 품질의 API를 제공할 수 있을 것입니다.</p>
                
                <hr>
                
                <h3>통합 리뷰: MRSummary.java</h3>
                <h4>1. 기능 설명</h4>
                <p>리뷰 대상 코드는 <code>MRSummary</code>라는 JPA 엔티티 클래스를 정의하고 있습니다. 이 클래스는 데이터베이스의 <code>mr_summary</code> 테이블과 매핑되며, 다음과 같은 필드를 포함하고 있습니다:</p>
                
                <ul>
                    <li><strong>id</strong>: 이 필드는 엔티티의 고유 식별자로, 자동 생성됩니다. <code>@GeneratedValue(strategy = GenerationType.IDENTITY)</code> 어노테이션을 사용하여 데이터베이스에서 ID를 자동으로 생성하도록 설정했습니다.</li>
                    <li><strong>gitlabEmail</strong>: GitLab 사용자 이메일을 저장하는 문자열 필드입니다. 이 필드는 <code>@Column</code> 어노테이션을 통해 데이터베이스의 <code>gitlab_email</code> 컬럼과 매핑됩니다.</li>
                    <li><strong>project</strong>: <code>Project</code> 엔티티와의 다대일 관계를 나타내는 필드입니다. <code>@ManyToOne(fetch = FetchType.LAZY)</code> 어노테이션을 사용하여 지연 로딩을 설정했습니다. 이는 성능 최적화에 기여할 수 있습니다.</li>
                    <li><strong>content</strong>: 요약 내용을 저장하는 문자열 필드입니다.</li>
                </ul>
                
                <p>이 클래스는 Lombok 라이브러리를 사용하여 getter 메서드를 자동으로 생성하고, 기본 생성자를 보호된 접근 수준으로 설정하여 외부에서 직접 인스턴스를 생성하지 못하도록 하고 있습니다.</p>
                
                <h4>2. 개선해야 할 사항</h4>
                <ul>
                    <li><strong>Validation</strong>: 현재 필드에 대한 유효성 검사가 없습니다. 예를 들어, <code>gitlabEmail</code>이나 <code>content</code> 필드에 대해 null 또는 빈 문자열을 허용하지 않도록 유효성 검사를 추가하는 것이 좋습니다. 이를 위해 <code>@NotNull</code>, <code>@NotEmpty</code> 등의 어노테이션을 사용할 수 있습니다.</li>
                    <li><strong>명확한 관계 설정</strong>: <code>project</code> 필드에 대한 관계가 명확하게 정의되어 있지만, <code>Project</code> 클래스에 대한 정보가 없으므로, 이 관계가 올바르게 설정되었는지 확인할 필요가 있습니다. <code>cascade</code> 옵션이나 <code>orphanRemoval</code> 설정을 고려할 수 있습니다.</li>
                    <li><strong>주석 추가</strong>: 각 필드에 대한 설명을 주석으로 추가하면 코드의 가독성을 높이고, 다른 개발자들이 이해하는 데 도움이 됩니다.</li>
                </ul>
                
                <h4>3. 수정 필요 항목</h4>
                <ul>
                    <li><strong>Nullability</strong>: <code>gitlabEmail</code>과 <code>content</code> 필드에 대해 null을 허용하지 않도록 설정하는 것이 좋습니다. 예를 들어:
                        <pre><code>@Column(name = "gitlab_email", nullable = false)
                private String gitlabEmail;
                
                @Column(nullable = false)
                private String content;
                </code></pre>
                    </li>
                    <li><strong>관계 설정</strong>: <code>project</code> 필드에 대한 관계 설정을 명확히 하고, 필요하다면 <code>cascade</code> 옵션을 추가하여 관련된 엔티티의 생명주기를 관리할 수 있습니다.</li>
                </ul>
                
                <h4>결론</h4>
                <p><code>MRSummary</code> 클래스는 기본적인 JPA 엔티티 구조를 잘 따르고 있으며, 데이터베이스와의 매핑이 명확하게 설정되어 있습니다. 그러나 유효성 검사와 관계 설정에 대한 추가적인 개선이 필요합니다. 이러한 개선 사항을 반영하면 코드의 안정성과 가독성을 높일 수 있을 것입니다.</p>
                
                
                """;
        gitlabServiceClient.addMergeRequestComment(824085L, 64L, "TWD9FX7P7Qc1bYqyo_cC", review);
    }
}