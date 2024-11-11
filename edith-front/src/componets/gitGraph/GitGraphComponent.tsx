import { Gitgraph, Branch, CommitOptions } from "@gitgraph/react";
import { useEffect, useState } from "react";
import { BranchData } from "../../types/gitGraphType";
import { gitGraphData } from "../../assets/dummyData";

const GitGraphComponent: React.FC = () => {
  const [data, setData] = useState<BranchData[] | undefined>([]);
  useEffect(() => {
    setData(gitGraphData);
  }, []);
  return (
    <Gitgraph>
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
