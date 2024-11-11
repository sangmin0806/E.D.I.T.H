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
      spacing: 30,
      label: {
        font: "14px Arial",
      },
    },
    commit: {
      spacing: 50,
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
  if (!data || data.length === 0) {
    return null;
  }

  return (
    <Gitgraph options={{ template: smallFontTemplate }}>
      {(gitgraph) => {
        const branches: { [key: string]: Branch } = {};

        // 초기 커밋을 추가하지 않고, 데이터에 따라 브랜치 및 커밋 생성
        data.forEach((branchData) => {
          const {
            sourceBranch,
            targetBranch,
            mergeCommit,
            sourceBranchCommits,
          } = branchData;

          // sourceBranch가 존재하지 않으면 새 브랜치를 생성
          if (!branches[sourceBranch]) {
            branches[sourceBranch] = gitgraph.branch(sourceBranch);
          }

          // sourceBranch에 커밋 추가
          sourceBranchCommits.forEach((commitData) => {
            branches[sourceBranch].commit({
              subject: commitData.message,
              author: commitData.author_name,
            });
          });

          // 병합 커밋 처리
          if (!branches[targetBranch]) {
            branches[targetBranch] = gitgraph.branch(targetBranch);
          }
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
