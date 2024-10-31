// projectData.ts

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
  1. 프로젝트 개요
  - 프로젝트 설명: '여행 한담'은 사용자가 여행지를 공유하고 추천하는 커뮤니티 플랫폼입니다. 사용자 맞춤형 추천 기능과 여행 일정 자동 생성 기능을 포함하며, 대규모 데이터를 실시간으로 처리할 수 있는 시스템을 구축했습니다.  
  - 주요 목표:
    - 사용자 참여도와 피드 추천 정확도 향상
    - 대규모 여행지 데이터 처리 성능 최적화
    - 사용자 맞춤형 여행 일정 자동 생성 기능 구현
  
  2. 핵심 기능
  
  2.1 실시간 피드 추천 시스템
  - 기능 설명: 사용자가 여행지, 숙소, 음식점에 대해 남긴 '좋아요'와 검색 기록을 기반으로 실시간 추천 피드를 생성합니다.  
  - 구체적인 구현:
    - 데이터 처리: Kafka와 Hadoop을 통해 사용자의 행동 데이터를 실시간으로 수집하고 분석.
    - 추천 알고리즘: 피드 추천 알고리즘은 사용자 유사도와 시간 가중치를 고려하여 추천 피드를 생성하며, Elasticsearch를 이용하여 빠른 검색과 필터링을 지원.  
  - 성과:
    - 사용자 피드 검색 속도 30% 향상.
    - 추천 정확도 15% 개선, 사용자 만족도 상승.
    - 실시간 피드 생성 기능으로 사용자 참여도 증가.
  
  2.2 여행 일정 자동 생성 
  - 기능 설명: 사용자가 선호하는 여행지, 동반자 수, 이동 거리 등을 기반으로 맞춤형 여행 일정을 자동으로 생성합니다.  
  - 구체적인 구현:
    - 데이터 분석: 사용자의 여행지 선호도와 과거 '좋아요' 데이터를 분석하여 개인화된 여행 경로를 생성.
    - 일정 최적화: 대규모 여행지 데이터를 처리하고, 각 여행지 간의 이동 거리를 계산하여 효율적인 여행 경로를 자동으로 생성.  
  - 성과:
    - 여행 일정 생성 시간 25% 단축.
    - 사용자의 여행 스타일에 맞춘 일정을 제공함으로써 만족도 향상.
    - 여행지 추천 경로의 정확도를 개선하여 일정의 효율성을 높임.
  
  2.3 사용자 리뷰 및 평가 시스템
  - 기능 설명: 사용자가 방문한 여행지, 숙소, 음식점에 대해 리뷰를 남기고, 다른 사용자들이 이를 평가할 수 있는 기능을 제공합니다.  
  - 구체적인 구현:
    - 평가 시스템: 리뷰에 대한 별점 및 텍스트 리뷰를 작성할 수 있으며, 긍정적인 리뷰에 따라 다른 사용자에게 추천될 확률이 증가.
    - 리뷰 필터링: Elasticsearch를 사용하여 리뷰 데이터를 검색하고, 최신 리뷰와 평점이 높은 리뷰를 우선적으로 표시.  
  - 성과:
    - 리뷰와 평가 데이터를 기반으로 여행지의 추천 신뢰도를 향상.
    - 사용자 간 상호작용을 활성화하여 플랫폼의 참여도 증가.
  
  2.4 사용자 인증 및 보안 시스템
  - 기능 설명: 사용자가 안전하게 로그인하고, 인증 절차를 거쳐 플랫폼에 접근할 수 있도록 하는 시스템입니다.  
  - 구체적인 구현:
    - JWT 기반 인증: 사용자의 로그인 세션을 JWT 토큰으로 관리하여, 보안성을 높이고 인증 절차를 간소화.
    - 세션 관리: Redis를 활용한 세션 관리로, 대규모 사용자 인증을 효율적으로 처리.  
  - 성과:
    - 사용자 인증 속도 30% 향상.
    - 보안성이 강화되어 불법 접근과 세션 하이재킹 방지.
    - 사용자 인증 실패율이 감소하고, 로그인 안정성 확보.
    `,
};

// Define an interface for the project list
interface ProjectListItem {
  id: number;
  subject: string;
  content: string;
  recentDate: Date; // Use Date for dates
  codeReview: boolean;
  teamMemberImg: string[]; // Array of strings for image URLs
}

// Create the project list data following the specified format
export const projectList: ProjectListItem[] = [
  {
    id: 1,
    subject: "Project Alpha",
    content: "Building a responsive web application.",
    recentDate: new Date("2024-10-15"), // Use Date for dates
    codeReview: true,
    teamMemberImg: [
      "https://example.com/img/member1.jpg",
      "https://example.com/img/member2.jpg",
      "https://example.com/img/member3.jpg",
    ],
  },
  {
    id: 2,
    subject: "Project Beta",
    content: "Implementing authentication and authorization.",
    recentDate: new Date("2024-10-20"), // Use Date for dates
    codeReview: false,
    teamMemberImg: [
      "https://example.com/img/member4.jpg",
      "https://example.com/img/member5.jpg",
    ],
  },
  {
    id: 3,
    subject: "Project Gamma",
    content: "Developing real-time data processing pipeline.",
    recentDate: new Date("2024-10-28"), // Use Date for dates
    codeReview: true,
    teamMemberImg: [
      "https://example.com/img/member6.jpg",
      "https://example.com/img/member7.jpg",
      "https://example.com/img/member8.jpg",
      "https://example.com/img/member9.jpg",
    ],
  },
];
