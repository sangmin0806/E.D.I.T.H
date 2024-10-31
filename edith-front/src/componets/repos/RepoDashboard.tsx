import ProjectCurrentState from "./ProjectCurrentState";

function RepoDashboard() {
  const data = { projcetCnt: 24, totalCommits: 1923, codeReviewCnt: 3 };
  return (
    <>
      <div className="flex flex-col gap-[2.5rem]">
        <ProjectCurrentState
          blueStateSubject={"총 커밋 수"}
          blueStateNum={data.projcetCnt}
          pinkStateSubject={"완료한 코드 리뷰"}
          pinkStateNum={data.totalCommits}
          yellowStateSubject={"오늘의 커밋 수"}
          yellowStateNum={data.codeReviewCnt}
        />
        <div className="py-8 pl-8 pr-8 bg-white/30 rounded-3xl flex-col justify-center items-center gap-6 inline-flex"></div>
      </div>
    </>
  );
}

export default RepoDashboard;
