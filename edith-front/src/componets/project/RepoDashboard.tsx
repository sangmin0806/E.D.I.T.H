import { useEffect, useState } from "react";
import ProjectCurrentState from "./ProjectCurrentState";
import GitGraphComponent from "../gitGraph/GitGraphComponent";
import blueLogo from "../../assets/edithBlueLogo.png";
import pinkLogo from "../../assets/edithPinkLogo.png";
import { techIcons } from "../../types/gitLogo";
import { Icon } from "@iconify/react";
import { getCommitStats } from "../../api/projectApi";
import { useParams } from "react-router-dom";
import { commitStat } from "../../types/projectType";

function RepoDashboard() {
  const [stat, setStat] = useState<commitStat>();
  const { projectID } = useParams();
  const numericProjectID = Number(projectID);
  const [loading, setLoading] = useState(true);

  const text = `
  - 파일: 파일명.java (라인 25-40)
  - 리뷰 유형: 필수 수정
  - 문제점: 중괄호 사용 규칙 미준수
  if (user != null) { user.setName("John"); } 중괄호 스타일을 수정해주세요.
  if (user != null) { user.setName("John"); }
  
`;
  const techStacks = ["JavaScript", "React", "Node.js", "AWS", "Docker"];
  const fixLogs: string[] = [
    "fix: Resolve issue where user authentication fails on Safari",
    "fix: Correct alignment issue in mobile view for navbar component",
    "fix: Address bug causing unexpected logout after token refresh",
    "fix: Update date format in user profile to match locale settings",
  ];

  useEffect(() => {
    getProjectStats();
    setLoading(false);
  }, []);

  const paragraphs = text
    .trim()
    .split("\n")
    .map((line, index) => (
      <p key={index} className="mb-2">
        {line.trim()}
      </p>
    ));

  const getProjectStats = async () => {
    try {
      const result = await getCommitStats(numericProjectID);
      if (!result.success) {
        throw new Error(result.error);
      }
      setStat(result.response);
    } catch (error) {
      alert(error);
    }
  };

  return (
    <>
      <div>
        <div className="flex flex-col gap-[2.5rem]">
          <ProjectCurrentState
            blueStateSubject={"총 커밋 수"}
            blueStateNum={stat?.totalCommitsCount || 0}
            pinkStateSubject={"완료한 코드 리뷰"}
            pinkStateNum={stat?.totalCodeReviewCount || 0}
            yellowStateSubject={"오늘의 커밋 수"}
            yellowStateNum={stat?.todayTotalCommitsCount || 0}
          />
          {/* 대시보드 첫번째 줄 */}
          <div className="py-8 pl-4 pr-4 bg-white/30 rounded-3xl flex-col justify-center items-center gap-6 inline-flex">
            <div className="flex w-[90%] gap-4">
              <div className="w-[40%] py-8 pl-8 pr-8 bg-white/30 rounded-3xl flex-col justify-center gap-6 inline-flex shadow-custom">
                <p className="text-xl font-semibold">최근 커밋 내역</p>
                <p>feqt: [FE] 로그인 기능 구현</p>
              </div>
              <div className="w-[60%] py-8 pl-8 pr-8 bg-white/30 rounded-3xl flex-col justify-center gap-6 inline-flex shadow-custom">
                <div className="flex gap-4">
                  <img src={blueLogo} className="w-[60px] h-auto" />
                  <p className="text-xl font-semibold">이디스의 조언</p>
                </div>
                <p>
                  git 컨벤션이 제대로 지켜지지 않는거 같아요
                  <br />
                  이에 대해 논의가 필요한 것으로 보입니다
                </p>
              </div>
            </div>
            {/* 두번째 줄 - 최근 코드 리뷰 */}
            <div className="w-[90%] py-8 pl-8 pr-8 bg-white/30 rounded-3xl flex-col justify-center gap-6 inline-flex shadow-custom">
              <div className="flex gap-4">
                <img src={pinkLogo} className="w-[60px] h-auto" />
                <p className="font-semibold">최근 코드 리뷰 - </p>
                <p>커밋 1: 로그인 오류 수정 (abcd1234) </p>
              </div>
              <p>
                유지 보수성을 높이기 위해 스타일 가이드를 준수하는 것이
                좋습니다.
              </p>
              <div className="columns-2 p-4 border border-gray-300 rounded-lg mb-4 space-y-2 text-sm">
                {paragraphs}
              </div>
            </div>

            {/* git graph */}
            <div className="w-[90%] py-8 pl-8 pr-8 bg-white/30 rounded-3xl flex-col justify-center gap-6 inline-flex shadow-custom">
              <p className="text-2xl font-semibold">Git History</p>
              <GitGraphComponent />
            </div>
            {/* 세번째 줄 */}
            <div className="flex gap-4 w-[90%]">
              {/* Tech Stacks */}
              <div className="w-[35%] py-8 pl-8 pr-8 bg-white/30 rounded-3xl flex-col justify-center gap-6 inline-flex shadow-custom">
                <p className="text-2xl font-semibold">Tech Stacks</p>
                <div className="flex flex-col space-y-4">
                  {techStacks.map((tech, index) => (
                    <div
                      key={index}
                      className="flex items-center py-4 px-2 space-x-4 hover:bg-gray-50 transition-colors duration-150 border-b border-gray-200"
                    >
                      {/* 아이콘이 없을 경우 기본 아이콘을 사용 */}
                      {techIcons[tech] ? (
                        <Icon icon={techIcons[tech]} width="28" height="28" />
                      ) : (
                        <img
                          src={blueLogo}
                          alt="default icon"
                          width="28"
                          height="28"
                        />
                      )}
                      <span className="text-base">{tech}</span>
                    </div>
                  ))}
                </div>
              </div>
              {/* Fix Logs */}
              <div className="w-[65%] py-8 pl-8 pr-8 bg-white/30 rounded-3xl flex-col justify-center gap-6 inline-flex shadow-custom">
                <p className="text-2xl font-semibold">Fix Log</p>
                <div className="flex flex-col space-y-4">
                  {fixLogs.map((fix, index) => (
                    <div
                      key={index}
                      className="flex items-center py-4 px-2 space-x-4 hover:bg-gray-50 transition-colors duration-150 border-b border-gray-200"
                    >
                      <p className="text-base">{fix}</p>
                    </div>
                  ))}
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </>
  );
}

export default RepoDashboard;
