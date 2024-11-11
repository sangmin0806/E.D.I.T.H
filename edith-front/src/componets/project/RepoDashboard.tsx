import { useEffect, useState } from "react";
import ProjectCurrentState from "./ProjectCurrentState";
import GitGraphComponent from "../gitGraph/GitGraphComponent";

function RepoDashboard() {
  const data = { projcetCnt: 24, totalCommits: 1923, codeReviewCnt: 3 };
  const [loading, setLoading] = useState(true);
  useEffect(() => {
    // 이건 나중에 지우기 !!
    const timer = setTimeout(() => {
      setLoading(false);
    }, 3000);

    // 컴포넌트가 언마운트될 때 타이머를 정리합니다.
    return () => clearTimeout(timer);
  }, []);
  return (
    <>
      <div>
        <div className="flex flex-col gap-[2.5rem]">
          <ProjectCurrentState
            blueStateSubject={"총 커밋 수"}
            blueStateNum={data.projcetCnt}
            pinkStateSubject={"완료한 코드 리뷰"}
            pinkStateNum={data.totalCommits}
            yellowStateSubject={"오늘의 커밋 수"}
            yellowStateNum={data.codeReviewCnt}
          />
          <div className="py-8 pl-8 pr-8 bg-white/30 rounded-3xl flex-col justify-center items-center gap-6 inline-flex">
            <div className="py-8 pl-8 pr-8 bg-white/30 rounded-3xl flex-col justify-center gap-6 inline-flex">
              <p className="text-xl font-semibold">Git History</p>
              <GitGraphComponent />
            </div>
          </div>
        </div>
      </div>
    </>
  );
}

export default RepoDashboard;
