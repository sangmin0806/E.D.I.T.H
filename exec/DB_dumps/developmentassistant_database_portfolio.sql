-- MySQL dump 10.13  Distrib 8.0.38, for Win64 (x86_64)
--
-- Host: 15.165.201.203    Database: developmentassistant_database
-- ------------------------------------------------------
-- Server version	8.0.40

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `portfolio`
--

DROP TABLE IF EXISTS `portfolio`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `portfolio` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_date` datetime(6) DEFAULT NULL,
  `last_modified_date` datetime(6) DEFAULT NULL,
  `content` longtext,
  `end_date` datetime(6) DEFAULT NULL,
  `start_date` datetime(6) DEFAULT NULL,
  `user_project_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK8qtwrg3eyw6opsjmsiqyp2u07` (`user_project_id`),
  CONSTRAINT `FKtf7crajm13frtrmg0drdjnw7s` FOREIGN KEY (`user_project_id`) REFERENCES `user_project` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `portfolio`
--

LOCK TABLES `portfolio` WRITE;
/*!40000 ALTER TABLE `portfolio` DISABLE KEYS */;
INSERT INTO `portfolio` VALUES (2,'2024-11-13 16:28:04.689227','2024-11-13 17:48:53.413896','```markdown\n# ? 개인화된 프로젝트 포트폴리오 ?\n\n## ? 프로젝트 설명\n이 프로젝트는 특정 Merge Request (MR)의 기록을 기반으로 개발자의 포트폴리오를 자동으로 생성하는 로직을 구현합니다. `langchain` 라이브러리를 활용하여 LLM(대형 언어 모델)과 메모리 기능을 사용하여, 개발자의 작업을 체계적으로 정리하고 포트폴리오를 자동으로 생성하여 시간과 노력을 절약할 수 있도록 돕습니다.\n\n## ?️ 기술 스택\n- **언어**: Python\n- **라이브러리**: langchain\n- **기타 도구**: Git, LLM\n\n## ? 핵심 로직\n1. **MR 기록 가져오기**: 특정 브랜치의 모든 MR 기록을 가져옵니다.\n2. **요약 생성**: MR ID별로 반복하여 요약을 생성하며, 이미 존재하는 요약은 메모리에 저장하고, 없을 경우 LLM에 질의하여 요약을 생성합니다.\n3. **포트폴리오 생성**: 최종적으로 메모리에 저장된 요약을 기반으로 개인화된 포트폴리오를 생성합니다.\n\n## ✨ 내가 구현한 내용\n- MR의 git diff를 기반으로 요약을 생성하고 메모리에 저장하는 기능을 구현했습니다.\n- 저장된 요약을 기반으로 개인화된 포트폴리오를 생성하는 로직을 작성했습니다.\n- 포트폴리오 내용에는 프로젝트 설명, 기술 스택, 핵심 로직, 구현 내용, 트러블 슈팅이 포함됩니다.\n\n## ? 트러블 슈팅\n- MR 제출 시, git diff 분석 과정에서 발생할 수 있는 오류를 처리하는 로직을 추가하여 안정성을 높였습니다.\n- 기술 스택 및 핵심 기능 요약 과정에서 발생할 수 있는 데이터 누락 문제를 해결하기 위해 예외 처리를 강화했습니다.\n\n---\n\n? **연락처**: doublehyun98@gmail.com\n```\n','2024-11-13 00:00:00.000000','2024-11-13 00:00:00.000000',9),(3,'2024-11-14 10:06:30.163429','2024-11-14 10:06:30.163429','```markdown\n# ? 개인화된 프로젝트 포트폴리오 ?\n\n## 1. 프로젝트 설명\n이 프로젝트는 특정 Merge Request (MR) 기록을 기반으로 개발자의 포트폴리오를 자동으로 생성하는 로직을 구현한 것입니다. Git의 diff 정보를 활용하여 각 MR에 대한 요약을 생성하고, 이를 바탕으로 최종 포트폴리오를 작성합니다. ?✨\n\n## 2. 기술 스택\n- **프로그래밍 언어**: Python\n- **프레임워크**: FastAPI\n- **데이터베이스**: PostgreSQL\n- **기타 도구**: Git, LLM (대형 언어 모델), ConversationBufferMemory, ChatPromptTemplate\n\n## 3. 핵심 로직\n- **MR 기록 가져오기**: 특정 브랜치의 모든 MR 기록을 가져오는 기능을 구현했습니다.\n- **요약 생성**: MR ID별로 반복하여 요약을 생성하며, 이미 존재하는 요약은 메모리에 저장하고, 없을 경우 LLM에 질의하여 요약을 생성합니다.\n- **포트폴리오 생성**: 생성된 요약을 바탕으로 개인화된 포트폴리오를 작성합니다. ?\n\n## 4. 내가 구현한 내용\n- `get_summary`: MR의 diff 정보를 기반으로 요약을 생성하고 메모리에 저장하는 기능을 구현했습니다. 요약 내용에는 기술 스택, 핵심 기능, 트러블 슈팅이 포함됩니다.\n- `get_portfolio`: 저장된 요약을 바탕으로 개인화된 포트폴리오를 생성하는 기능을 구현했습니다.\n- `make_portfolio`: 전체 포트폴리오 생성 프로세스를 관리하는 기능을 구현했습니다. ?\n\n## 5. 트러블 슈팅\n- MR 요약 생성 시 발생할 수 있는 오류를 처리하기 위해 예외 처리를 추가했습니다.\n- LLM에 질의할 때 발생할 수 있는 네트워크 오류를 대비하여 재시도 로직을 구현했습니다. ?️\n\n---\n\n이 포트폴리오는 개발자의 작업을 효과적으로 정리하고, 자동으로 생성하는 데 유용합니다. ?\n```','2024-11-13 00:00:00.000000','2024-11-13 00:00:00.000000',10),(4,'2024-11-15 10:34:49.798447','2024-11-15 10:34:49.798447','```markdown\n# ? 프로젝트 포트폴리오 ?\n\n## 개발자 정보\n- **이메일**: sangmin@ssafy.com\n\n---\n\n## 1. 프로젝트 설명\n이 프로젝트는 특정 Merge Request (MR)의 기록을 기반으로 개발자의 포트폴리오를 자동으로 생성하는 로직을 구현합니다. Git의 diff 정보를 활용하여 각 MR에 대한 요약을 생성하고, 이를 바탕으로 개인화된 포트폴리오를 작성합니다. ?✨\n\n## 2. 기술 스택\n- **LangChain**: 대형 언어 모델과의 상호작용을 위한 라이브러리\n- **UUID**: 고유 식별자를 생성하여 각 포트폴리오 인스턴스를 구분\n- **Python**: 프로그래밍 언어\n- **Git**: 버전 관리 시스템\n\n## 3. 핵심 로직\n- **MR 기록 가져오기**: 특정 브랜치의 모든 MR 기록을 가져옵니다.\n- **요약 생성**: 각 MR에 대해 요약이 존재하는 경우 메모리에 저장하고, 없을 경우 LLM을 통해 요약을 생성합니다.\n- **포트폴리오 생성**: 저장된 요약을 바탕으로 개인화된 포트폴리오를 생성합니다.\n\n## 4. 내가 구현한 내용\n- `get_summary(llm, memory, merge_requests, summaries_dict)`: MR의 diff 정보를 기반으로 요약을 생성하고, 요약이 이미 존재하는 경우 메모리에 저장합니다.\n- `get_portfolio(llm, memory, user_id, memory_key)`: 저장된 요약을 바탕으로 최종 포트폴리오를 생성합니다.\n- `make_portfolio(user_id, summaries, merge_requests)`: 포트폴리오 생성을 위한 메인 함수로, MR 요약을 생성하고 최종 포트폴리오를 반환합니다.\n- `generate_uuid()`: 고유한 UUID를 생성하여 포트폴리오 메모리 키로 사용합니다.\n\n## 5. 트러블 슈팅\n- 요약 생성 과정에서 LLM의 응답이 불완전할 경우, 추가적인 프롬프트를 통해 더 나은 결과를 얻도록 개선하였습니다. \n- MR 기록이 누락되는 경우, Git의 API를 통해 다시 데이터를 가져오는 로직을 추가하여 안정성을 높였습니다. ??\n\n---\n\n이 포트폴리오는 개발자가 자신의 기여를 효과적으로 정리하고, 기술과 경험을 보여줄 수 있도록 돕기 위해 설계되었습니다. ?\n```','2024-11-13 00:00:00.000000','2024-11-13 00:00:00.000000',20),(5,'2024-11-18 10:23:53.332090','2024-11-18 10:23:53.332090','<!DOCTYPE html><html lang=\"ko\"><head>    <meta charset=\"UTF-8\">    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">    <title>AI 코드리뷰 및 포트폴리오 자동 생성 프로젝트</title>    <style>        body {            font-family: Arial, sans-serif;            line-height: 1.6;            margin: 20px;            padding: 20px;            background-color: #f4f4f4;        }        h1, h2, h3, h4 {            color: #333;        }        ul {            list-style-type: disc;            margin-left: 20px;        }        pre {            background-color: #eaeaea;            padding: 10px;            border-radius: 5px;        }    </style></head><body>    <h1>AI 코드리뷰 및 포트폴리오 자동 생성 프로젝트</h1>    <h2>프로젝트 설명</h2>    <p>이 프로젝트는 AI 기반 코드 리뷰 및 포트폴리오 자동 생성 시스템으로, 개발자들이 Git Merge Request(MR)의 파일별 diff를 기반으로 포트폴리오를 생성할 수 있도록 돕는 서비스입니다. 사용자는 자신의 GitHub 또는 GitLab 계정과 연동하여, 특정 프로젝트에 대한 요약 및 포트폴리오를 자동으로 생성할 수 있습니다.</p>    <h2>기술 스택</h2>    <ul>        <li>프론트엔드: React, TypeScript</li>        <li>백엔드: Spring Boot, Flask</li>        <li>데이터베이스: Redis, MySQL</li>        <li>기타: WebSocket, JWT, REST API</li>    </ul>    <h2>핵심 로직</h2>    <h3>1. 포트폴리오 생성 로직</h3>    <p>사용자의 GitLab API를 통해 Merge Request 정보를 수집하고, 이를 기반으로 포트폴리오를 생성하는 로직을 구현하였습니다. 포트폴리오 생성 요청 시 프로젝트 설명과 사용자 이메일을 포함하여 요청을 처리합니다.</p>    <h3>2. 얼굴 인식 로그인</h3>    <p>사용자가 카메라 앞에서 얼굴을 인식하여 로그인할 수 있는 기능을 구현하였습니다. 얼굴 인식 모델을 로드하고, 사용자의 카메라를 설정하여 실시간으로 얼굴을 감지합니다.</p>    <h3>3. WebSocket 통신</h3>    <p>서버와 클라이언트 간의 실시간 데이터 전송을 위한 WebSocket 구현을 통해 로그인 성공 및 실패 시 사용자에게 적절한 메시지를 표시합니다.</p>    <h2>트러블 슈팅</h2>    <h3>1. 모델 로딩 실패</h3>    <p>얼굴 인식 모델 로딩 시 발생할 수 있는 오류를 처리하기 위한 try-catch 블록을 추가하였습니다.</p>    <h3>2. WebSocket 연결 문제</h3>    <p>WebSocket 연결이 실패할 경우 오류 로그를 기록하여 문제를 추적할 수 있도록 개선하였습니다.</p>    <h3>3. 상태 관리 문제</h3>    <p>컴포넌트 언마운트 시 상태를 정리하는 로직을 추가하여 메모리 누수를 방지하였습니다.</p>    <h2>내가 구현한 내용</h2>    <h3>담당 파트</h3>    <p>주요 기능으로는 포트폴리오 생성 요청 처리, 얼굴 인식 로그인 기능, WebSocket 통신 구현, API 요청 및 응답 처리 로직을 담당하였습니다.</p>    <h3>기술 스택</h3>    <ul>        <li>프론트엔드: React, TypeScript, face-api.js</li>        <li>백엔드: Spring Boot, Flask</li>        <li>데이터베이스: Redis, MySQL</li>        <li>기타: WebSocket, JWT, REST API</li>    </ul>    <h3>트러블 슈팅 내역</h3>    <ul>        <li>모델 로딩 실패 시 오류 메시지를 콘솔에 출력하도록 개선하였습니다.</li>        <li>WebSocket이 열리지 않았을 때 재시도 로직을 추가하여 사용자에게 피드백을 제공하였습니다.</li>        <li>API 호출 시 발생할 수 있는 예외를 처리하여 로그를 남기고, 사용자에게 적절한 오류 메시지를 반환하도록 개선하였습니다.</li>    </ul>    <h2>개발자 정보</h2>    <p>개발자 ID: <a href=\"mailto:marmong9770@gmail.com\">marmong9770@gmail.com</a></p></body></html> 이 HTML 문서는 프로젝트의 전반적인 설명, 기술 스택, 핵심 로직, 트러블 슈팅 내역, 그리고 개인의 기여 내용을 포함하고 있습니다. 각 섹션은 명확하게 구분되어 있어 읽기 쉽고, 필요한 정보를 쉽게 찾을 수 있도록 구성되어 있습니다.','2024-11-18 00:00:00.000000','2024-11-14 00:00:00.000000',31);
/*!40000 ALTER TABLE `portfolio` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-11-19  0:46:13
