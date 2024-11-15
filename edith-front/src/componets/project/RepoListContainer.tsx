import ProjectCurrentState from "./ProjectCurrentState";
import RepoList from "./RepoList";
import PlusSquareImg from "../../assets/plus_sqare.png";
import { useComponentStore } from "../../store/repoPageStore";
import { useEffect, useState } from "react";
import LoadingSpinner from "../common/LoadingSpinner";
import { getMyCommitsStats } from "../../api/projectApi";
import { myCommitStat } from "../../types/projectType";

function RepoListContainer() {
  //API 통신 후 받게 될 data
  const [loading, setLoading] = useState(true);
  const [stat, setStat] = useState<myCommitStat>();
  const toggleComponent = useComponentStore((state) => state.toggleComponent);

  const handleMoveToAdd = () => {
    toggleComponent(2);
  };
  useEffect(() => {
    getCommitStats();
    setLoading(false);
  }, []);
  const getCommitStats = async () => {
    try {
      const result = await getMyCommitsStats();
      if (!result.success) {
        throw new Error(result.error);
      }
      console.log("stat : " + result.response);
      setStat(result.response);
    } catch (error) {
      alert(error);
    }
  };
  if (loading) {
    return <LoadingSpinner />;
  }

  return (
    <>
      <div className="flex flex-col gap-[2.5rem]">
        <ProjectCurrentState
          blueStateSubject={"총 프로젝트 수"}
          blueStateNum={stat?.totalProjectsCount || 0}
          pinkStateSubject={"오늘 나의 커밋 수"}
          pinkStateNum={stat?.todayCommitsCount || 0}
          yellowStateSubject={"코드 리뷰 진행중"}
          yellowStateNum={stat?.todayMergeRequestsCount || 0}
        />
        <div className="py-8 pl-8 pr-8 bg-white/30 rounded-3xl flex-col justify-center items-center gap-6 inline-flex">
          <div className="flex w-full flex-col gap-6">
            <div className="flex gap-2 items-center">
              <p className=" p-2 font-semibold text-2xl">
                새로운 프로젝트 추가
              </p>
              <img
                className="w-9 h-9 cursor-pointer"
                src={PlusSquareImg}
                onClick={handleMoveToAdd}
              />
            </div>
            <RepoList />
          </div>
        </div>
      </div>
    </>
  );
}
export default RepoListContainer;
