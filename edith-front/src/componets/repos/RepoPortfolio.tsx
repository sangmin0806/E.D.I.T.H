import { travelCommunityProject } from "../../assets/dummyData";
import edithLogo from "../../assets/edithLogo.png";
interface portfolioProp {
  userGitAccount: string;
}
function RepoPortfolio({ userGitAccount }: portfolioProp) {
  const data = travelCommunityProject;
  return (
    <>
      <div className="flex flex-col mx-4 gap-8">
        <div className="flex gap-3">
          <img src={edithLogo} />
          <p className="font-semibold text-lg">{userGitAccount} Portfolio</p>
        </div>
        <div className="flex flex-col gap-4">
          <div className="px-6 py-10 bg-white/30 rounded-3xl justify-center items-start gap-2.5 inline-flex flex-col">
            <p className="font-semibold text-lg">
              프로젝트 : {data.projectName}
            </p>
            <p className="font-semibold text-lg">
              기간 : {data.startDate} ~ {data.endDate}
            </p>
          </div>
          <div className="py-8 pl-8 pr-8 mb-8 bg-white/60 rounded-3xl flex-col justify-center items-center gap-6 inline-flex">
            <p>{data.contents}</p>
          </div>
        </div>
      </div>
    </>
  );
}

export default RepoPortfolio;
