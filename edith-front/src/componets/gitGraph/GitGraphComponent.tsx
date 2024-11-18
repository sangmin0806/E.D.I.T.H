import {
  Gitgraph,
  Branch,
  templateExtend,
  TemplateName,
} from "@gitgraph/react";
import { useEffect, useState } from "react";
import { BranchData } from "../../types/gitGraphType";
import { useParams } from "react-router-dom";
import { getGitGraphRequest } from "../../api/projectApi";

const GitGraphComponent: React.FC = () => {
  const { projectID } = useParams();
  const numericUserId = Number(projectID);
  const [data, setData] = useState<BranchData[]>([]);
  const [loading, setLoading] = useState(true);

  const smallFontTemplate = templateExtend(TemplateName.Metro, {
    branch: {
      lineWidth: 4,
      spacing: 20,
      label: {
        font: "12px Arial",
      },
    },
    commit: {
      spacing: 50,
      dot: {
        size: 10,
      },
      message: {
        display: true,
        font: "12px Arial",
      },
    },
  });

  // 메시지를 40자로 제한하는 함수
  const truncateMessage = (message: string, maxLength: number = 100) => {
    return message.length > maxLength
      ? `${message.slice(0, maxLength)}...`
      : message;
  };

  useEffect(() => {
    const fetchData = async () => {
      try {
        setLoading(true);
        const result = await getGitGraphRequest(numericUserId);
        if (!result.success) {
          throw new Error(result.error);
        }
        setData(result.response || []);
        console.log(result.response);
      } catch (error) {
        console.error("Error fetching graph data:", error);
        alert("Failed to load data");
      }
    };

    fetchData();
  }, []);

  useEffect(() => {
    if (data.length > 0 || loading) {
      console.log("Fetched data:", data);
      setLoading(false);
    }
  }, [data]);

  return (
    <>
      {loading ? (
        <p>Loading data...</p>
      ) : data.length > 0 ? (
        <Gitgraph options={{ template: smallFontTemplate }}>
          {(gitgraph) => {
            const branches: { [key: string]: Branch } = {};
              console.log("data: ", data)
              if (!branches["develop"]) {
                branches["develop"] = gitgraph.branch("develop");
              }            
              data.reverse().forEach((branchData) => {
                const {
                  sourceBranch,
                  targetBranch,
                  mergeCommit,
                  sourceBranchCommits,
                } = branchData;
                // console.log("source: ", sourceBranch)
                // console.log("target: ", targetBranch)
                // console.log("mergeCOmmit: ", mergeCommit)
                // console.log("sourceBranchCOmmits: ", sourceBranchCommits)
                if (!branches[sourceBranch]) {
                  branches[sourceBranch] = gitgraph.branch(sourceBranch);
                }
                // console.log("branches[sb]", branches[sourceBranch])
                sourceBranchCommits.forEach((commitData) => {
                  console.log("commitData", commitData);
                  branches[sourceBranch].commit({
                    hash: commitData.id,
                    subject: truncateMessage(commitData.message),
                    author: `${commitData.author_name} <${commitData.author_email}>`,
                  });
                });

                // if (!branches[targetBranch]) {
                //   branches[targetBranch] = gitgraph.branch(targetBranch);
                // }
                // console.log("branches[tb]", branches[targetBranch])
                branches["develop"]
                  .merge({
                      branch: branches[sourceBranch],
                    commitOptions: {
                        hash: mergeCommit.id,
                        subject: truncateMessage(mergeCommit.message),
                        author: `${mergeCommit.author_name} <${mergeCommit.author_email}>`,
                      }
                  })
                // .commit({
                //   subject: truncateMessage(mergeCommit.message),
                //   author: mergeCommit.author_name,
                // });
            });
          }}
        </Gitgraph>
      ) : (
        <p>No data available.</p>
      )}
    </>
  );
};

export default GitGraphComponent;
