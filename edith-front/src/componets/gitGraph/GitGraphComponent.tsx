import {
  Gitgraph,
  Branch,
  templateExtend,
  TemplateName,
} from "@gitgraph/react";
import { useEffect, useState } from "react";
import { BranchData } from "../../types/gitGraphType";
import { gitGraphData } from "../../assets/dummyData";

const GitGraphComponent: React.FC = () => {
  const [data, setData] = useState<BranchData[] | undefined>();
  const smallFontTemplate = templateExtend(TemplateName.Metro, {
    branch: {
      lineWidth: 4,
      label: {
        font: "14px Arial",
      },
    },
    commit: {
      dot: {
        size: 10, // 커밋 점 크기 (기본값은 12)
      },
      message: {
        display: true,
        font: "14px Arial",
      },
    },
  });

  useEffect(() => {
    setData(gitGraphData); // 실제로는 API 호출 결과를 여기서 설정
  }, []);

  const formatMessage = (message: string) => {
    // 글자 수에 따라 두 줄로 나누기
    const maxLength = 20; // 각 줄의 최대 글자 수
    if (message.length > maxLength) {
      return `${message.slice(0, maxLength)}<br />${message.slice(maxLength)}`;
    }
    return message;
  };

  if (!data || data.length === 0) {
    return null;
  }

  return (
    <Gitgraph options={{ template: smallFontTemplate }}>
      {(gitgraph) => {
        const branches: { [key: string]: Branch } = {};

        // 메인 브랜치 (develop) 생성
        branches["develop"] = gitgraph.branch("develop").commit("초기 커밋");

        // response 데이터를 사용하여 각 브랜치와 커밋을 생성
        data?.forEach((branchData) => {
          const {
            sourceBranch,
            targetBranch,
            mergeCommit,
            sourceBranchCommits,
          } = branchData;

          // 브랜치가 존재하지 않으면 생성
          if (!branches[sourceBranch]) {
            branches[sourceBranch] = gitgraph.branch(sourceBranch);
          }

          // 각 커밋을 sourceBranch에 추가
          sourceBranchCommits.forEach((commitData) => {
            branches[sourceBranch].commit({
              subject: commitData.message,
              author: commitData.author_name,
            });
          });

          // 병합 커밋 처리
          branches[targetBranch]
            .merge(branches[sourceBranch], mergeCommit.message)
            .commit({
              subject: mergeCommit.message,
              author: mergeCommit.author_name,
            });
        });
      }}
    </Gitgraph>
  );
};

export default GitGraphComponent;
