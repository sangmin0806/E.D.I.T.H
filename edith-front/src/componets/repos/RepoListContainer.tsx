import ProjectCurrentState from "./ProjectCurrentState";
import RepoList from "./RepoList";
import PlusSquareImg from "../../assets/plus_sqare.png";
import { useComponentStore } from "../../store/repoPageStore";
import { useEffect, useState } from "react";

function RepoListContainer() {
  //API 통신 후 받게 될 data
  const data = { projcetCnt: 24, totalCommits: 1923, codeReviewCnt: 3 };
  const [loading, setLoading] = useState(true);
  const toggleComponent = useComponentStore((state) => state.toggleComponent);
  useEffect(() => {
    setLoading(false);
  }, []);
  if (loading) {
    return <p>로딩 중...</p>;
  }

  return (
    <>
      <div className="flex flex-col gap-[2.5rem]">
        <ProjectCurrentState
          blueStateSubject={"총 프로젝트 수"}
          blueStateNum={data.projcetCnt}
          pinkStateSubject={"오늘 나의 커밋 수"}
          pinkStateNum={data.totalCommits}
          yellowStateSubject={"코드 리뷰 진행중"}
          yellowStateNum={data.codeReviewCnt}
        />
        <div className="py-8 pl-8 pr-8 bg-white/30 rounded-3xl flex-col justify-center items-center gap-6 inline-flex">
          <div className="flex w-full flex-col gap-6">
            <div className="flex gap-2 items-center">
              <p className=" p-2 font-semibold text-2xl">
                새로운 프로젝트 추가
              </p>
              <img
                className="w-9 h-9"
                src={PlusSquareImg}
                onClick={toggleComponent}
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
