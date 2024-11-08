// projectData.ts

import { ProjectListItem } from "../types/projectType";

// Define an interface for the project data
interface Project {
  projectName: string;
  startDate: string; // Use string for dates in the specified format
  endDate: string; // Use string for dates in the specified format
  contents: string; // Markdown content as a string
}

// Create the project data following the specified format
export const travelCommunityProject: Project = {
  projectName: "여행 한담",
  startDate: "2023-03-01",
  endDate: "2023-12-30",
  contents: `
  # 1. 프로젝트 개요
- **프로젝트 설명**: '여행 한담'은 사용자가 여행지를 공유하고 추천하는 커뮤니티 플랫폼입니다. 사용자 맞춤형 추천 기능과 여행 일정 자동 생성 기능을 포함하며, 대규모 데이터를 실시간으로 처리할 수 있는 시스템을 구축했습니다.  
- **주요 목표**:
  - 사용자 참여도와 피드 추천 정확도 향상
  - 대규모 여행지 데이터 처리 성능 최적화
  - 사용자 맞춤형 여행 일정 자동 생성 기능 구현
  
# 2. 핵심 기능

## 2.1 실시간 피드 추천 시스템
- **기능 설명**: 사용자가 여행지, 숙소, 음식점에 대해 남긴 '좋아요'와 검색 기록을 기반으로 실시간 추천 피드를 생성합니다.  
- **구체적인 구현**:
  - 데이터 처리: Kafka와 Hadoop을 통해 사용자의 행동 데이터를 실시간으로 수집하고 분석합니다.
  - 추천 알고리즘: 사용자 유사도와 시간 가중치를 고려하여 추천 피드를 생성하며, Elasticsearch를 이용하여 빠른 검색과 필터링을 지원합니다.  
- **성과**:
  - 사용자 피드 검색 속도 30% 향상.
  - 추천 정확도 15% 개선, 사용자 만족도 상승.
  - 실시간 피드 생성 기능으로 사용자 참여도 증가.
  
## 2.2 여행 일정 자동 생성
- **기능 설명**: 사용자가 선호하는 여행지, 동반자 수, 이동 거리 등을 기반으로 맞춤형 여행 일정을 자동으로 생성합니다.  
- **구체적인 구현**:
  - 데이터 분석: 사용자의 여행지 선호도와 과거 '좋아요' 데이터를 분석하여 개인화된 여행 경로를 생성합니다.
  - 일정 최적화: 대규모 여행지 데이터를 처리하고, 각 여행지 간의 이동 거리를 계산하여 효율적인 여행 경로를 자동으로 생성합니다.  
- **성과**:
  - 여행 일정 생성 시간 25% 단축.
  - 사용자의 여행 스타일에 맞춘 일정을 제공함으로써 만족도 향상.
  - 여행지 추천 경로의 정확도를 개선하여 일정의 효율성을 높임.
  
## 2.3 사용자 리뷰 및 평가 시스템
- **기능 설명**: 사용자가 방문한 여행지, 숙소, 음식점에 대해 리뷰를 남기고, 다른 사용자들이 이를 평가할 수 있는 기능을 제공합니다.  
- **구체적인 구현**:
  - 평가 시스템: 리뷰에 대한 별점 및 텍스트 리뷰를 작성할 수 있으며, 긍정적인 리뷰에 따라 다른 사용자에게 추천될 확률이 증가합니다.
  - 리뷰 필터링: Elasticsearch를 사용하여 리뷰 데이터를 검색하고, 최신 리뷰와 평점이 높은 리뷰를 우선적으로 표시합니다.  
- **성과**:
  - 리뷰와 평가 데이터를 기반으로 여행지의 추천 신뢰도를 향상.
  - 사용자 간 상호작용을 활성화하여 플랫폼의 참여도 증가.
  
## 2.4 사용자 인증 및 보안 시스템
- **기능 설명**: 사용자가 안전하게 로그인하고, 인증 절차를 거쳐 플랫폼에 접근할 수 있도록 하는 시스템입니다.  
- **구체적인 구현**:
  - JWT 기반 인증: 사용자의 로그인 세션을 JWT 토큰으로 관리하여 보안성을 높이고 인증 절차를 간소화합니다.
  - 세션 관리: Redis를 활용한 세션 관리로 대규모 사용자 인증을 효율적으로 처리합니다.  
- **성과**:
  - 사용자 인증 속도 30% 향상.
  - 보안성이 강화되어 불법 접근과 세션 하이재킹 방지.
  - 사용자 인증 실패율이 감소하고, 로그인 안정성 확보.
  `,
};

// Create the project list data following the specified format
export const projectList: ProjectListItem[] = [
  {
    id: 824085,
    url: null,
    name: "Example Project",
    token: "GfQ_e1hqiiaovywByhyd",
    contents:
      "This is an example project description with detailed information.",
    branches: [
      {
        id: 1,
        name: "main",
      },
      {
        id: 2,
        name: "develop",
      },
      {
        id: 3,
        name: "feature/new-feature",
      },
      {
        id: 4,
        name: "test",
      },
    ],
    updatedAt: "2024-11-08",
    contributors: [
      {
        name: "Sanghyun Lee",
        avatarUrl:
          "https://secure.gravatar.com/avatar/d28a5f01188ed324cd3a4eae7528f21b120ccf579222e1ec4d5b24c433bb7b4f?s=80&d=identicon",
      },
      {
        name: "TaeHee-Lee",
        avatarUrl:
          "https://secure.gravatar.com/avatar/685a4857f9f3aba0ed616b0e5c1f1b03552885b7166ae3e2fe367f262137ddc6?s=80&d=identicon",
      },
      {
        name: "Sangjin RYU",
        avatarUrl:
          "https://lab.ssafy.com/uploads/-/system/user/avatar/547/avatar.png",
      },
      {
        name: "choiinkuk",
        avatarUrl:
          "https://secure.gravatar.com/avatar/71419dd1ff37e7592bc8a86c60eeba73a02c83c4edc806253f3ffa3839e86d72?s=80&d=identicon",
      },
      {
        name: "Sungjoon Kim",
        avatarUrl:
          "https://secure.gravatar.com/avatar/4a8162cae2aae230a1066d73399032443e89415436a97abfb25a1fb7521ab264?s=80&d=identicon",
      },
      {
        name: "한기철",
        avatarUrl:
          "https://secure.gravatar.com/avatar/da6cf26c03f79efa8def9761aaeba7077af8719769d65a4988972231ee3d7bc2?s=80&d=identicon",
      },
      {
        name: "최호근",
        avatarUrl:
          "https://lab.ssafy.com/uploads/-/system/user/avatar/1084/avatar.png",
      },
      {
        name: "김신일",
        avatarUrl:
          "https://secure.gravatar.com/avatar/9e1e7fb0236cd5318af2abb9e4509c1cc95ab8f9812471e86f69ed419464bddb?s=80&d=identicon",
      },
      {
        name: "박찬국",
        avatarUrl:
          "https://secure.gravatar.com/avatar/d747270063e9f58a0329b1acb475b4b6d556f1f223f462fdf2569a1bdecddf5c?s=80&d=identicon",
      },
      {
        name: "김성재",
        avatarUrl:
          "https://secure.gravatar.com/avatar/b31772e34dbf399f7a2c0f7e62311958df85e6c44cd1ce7ac95b1dcd76c0fbf3?s=80&d=identicon",
      },
      {
        name: "서성수",
        avatarUrl:
          "https://secure.gravatar.com/avatar/e4c133f4266f08c8f3e398517c29cb17216a3cf5537c1eadc73a2b8e60ae21e5?s=80&d=identicon",
      },
      {
        name: "김민정",
        avatarUrl:
          "https://secure.gravatar.com/avatar/38c18aaa2942aaf3210edb56349dc64eed1a1eb43b06fd5e2396b50d73349524?s=80&d=identicon",
      },
      {
        name: "강시몬",
        avatarUrl:
          "https://secure.gravatar.com/avatar/9e57014e14368868b08d88a83b4b154e968609c8a8ae9475fa403ff054779553?s=80&d=identicon",
      },
      {
        name: "이승윤",
        avatarUrl:
          "https://secure.gravatar.com/avatar/228deae4f9775c163bdf5246b1bc4d75ff93e4065ebc4eb3db54b48037b59417?s=80&d=identicon",
      },
      {
        name: "박세영",
        avatarUrl:
          "https://lab.ssafy.com/uploads/-/system/user/avatar/7045/avatar.png",
      },
      {
        name: "정용기",
        avatarUrl:
          "https://secure.gravatar.com/avatar/020b94d5deb0093fd5244d6da90519b3617378c919286748ddeee0fd4763e4c9?s=80&d=identicon",
      },
      {
        name: "오형남",
        avatarUrl:
          "https://secure.gravatar.com/avatar/423bd2d2ba8dd1d3c4edb77a3533dc2072d8c4a50e43066e585fcee5b59b02e2?s=80&d=identicon",
      },
      {
        name: "고성현",
        avatarUrl:
          "https://secure.gravatar.com/avatar/8cbfe854136e4fc07baf7ecd6258f745265b7676624c04a5c79c152964fc7216?s=80&d=identicon",
      },
      {
        name: "이현석",
        avatarUrl:
          "https://secure.gravatar.com/avatar/329c8b5eb96dc17937637b96921167b71919176eb9135bbf45bd4272abe8c25f?s=80&d=identicon",
      },
      {
        name: "구자용",
        avatarUrl:
          "https://secure.gravatar.com/avatar/b067d34bfab7b7a0efff85df2bd97711404d45c3a44130eb389c6ceccfa9732a?s=80&d=identicon",
      },
    ],
  },
];

interface PortfolioData {
  id: number;
  portfolioName: string;
  repoProjectName: string;
  savedDate: Date; // 날짜 형식은 문자열로 설정
}

// 더미 데이터 생성
export const dummyPortfolioData: PortfolioData[] = [
  {
    id: 1,
    portfolioName: "Front-End Projects Collection",
    repoProjectName: "여행 한담",
    savedDate: new Date("2024-11-01"),
  },
  {
    id: 2,
    portfolioName: "Back-End Development Series",
    repoProjectName: "Real-Time Chat Application",
    savedDate: new Date("2024-10-25"),
  },
  {
    id: 3,
    portfolioName: "Data Visualization Works",
    repoProjectName: "Sales Dashboard",
    savedDate: new Date("2024-09-15"),
  },
  {
    id: 4,
    portfolioName: "Blockchain Solutions",
    repoProjectName: "NFT Marketplace",
    savedDate: new Date("2024-08-10"),
  },
  {
    id: 5,
    portfolioName: "Cloud-Based Applications",
    repoProjectName: "Microservices with AWS",
    savedDate: new Date("2024-07-05"),
  },
];
