import { useEffect, useState } from "react";
import ProjectCurrentState from "./ProjectCurrentState";
import GitGraphComponent from "../gitGraph/GitGraphComponent";
import blueLogo from "../../assets/edithBlueLogo.png";
import pinkLogo from "../../assets/edithPinkLogo.png";
import { techIcons } from "../../types/gitLogo";
import { Icon } from "@iconify/react";
import { getCommitStats, getDashboard } from "../../api/projectApi";
import { useParams } from "react-router-dom";
import { commitStat, projectDashboard } from "../../types/projectType";

function RepoDashboard() {
  const [stat, setStat] = useState<commitStat>();
  const [dashboard, setDashboard] = useState<projectDashboard>();
  const { projectID } = useParams();
  const numericProjectID = Number(projectID);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    getProjectStats();
    getProjectDashboard();
  }, []);

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

  const getProjectDashboard = async () => {
    try {
      const result = await getDashboard(numericProjectID);
      if (!result.success) {
        throw new Error(result.error);
      }
      setDashboard(result.response);
      setLoading(false);
    } catch (error) {
      alert(error);
    }
  };

  return (
    <>
      <div>
        <div className="flex flex-col gap-[2.5rem]">
          <ProjectCurrentState
            blueStateSubject={"총 MR 수"}
            blueStateNum={stat?.totalMergeRequestCount || 0}
            pinkStateSubject={"오늘의 MR 수"}
            pinkStateNum={stat?.todayMergeRequestsCount || 0}
            yellowStateSubject={"오늘의 커밋 수"}
            yellowStateNum={stat?.todayCommitsCount || 0}
          />
          {/* 대시보드 첫번째 줄 */}
          <div className="py-8 pl-4 pr-4 bg-white/30 rounded-3xl flex-col justify-center items-center gap-6 inline-flex">
            <div className="flex w-[90%] gap-4">
              <div className="w-[40%] py-8 pl-8 pr-8 bg-white/30 rounded-3xl flex-col justify-center gap-6 inline-flex shadow-custom">
                <p className="text-xl font-semibold">최근 커밋 내역</p>
                <p>{dashboard?.recentCommitMessage}</p>
              </div>
              <div className="w-[60%] py-8 pl-8 pr-8 bg-white/30 rounded-3xl flex-col justify-center gap-6 inline-flex shadow-custom">
                <div className="flex gap-4">
                  <img src={blueLogo} className="w-[60px] h-auto" />
                  <p className="text-xl font-semibold">이디스의 조언</p>
                </div>
                <p>{dashboard?.advice}</p>
              </div>
            </div>
            {/* 두번째 줄 - 최근 코드 리뷰 */}
            <div className="w-[90%] py-8 pl-8 pr-8 bg-white/30 rounded-3xl flex-col justify-center gap-6 inline-flex shadow-custom">
              <div className="flex gap-4">
                <img src={pinkLogo} className="w-[60px] h-auto" />
                <p className="font-semibold">최근 코드 리뷰 </p>
              </div>
              <div className="columns-2 p-4 border border-gray-300 rounded-lg mb-4 space-y-2 text-sm">
                {dashboard?.recentCodeReview}
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
                  {dashboard?.techStack.map((tech, index) => (
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
                  {dashboard?.fixLogs.map((fix, index) => (
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
