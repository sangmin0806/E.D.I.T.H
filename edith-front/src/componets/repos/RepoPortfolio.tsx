import { travelCommunityProject } from "../../assets/dummyData";
import ReactMarkdown from "react-markdown";
import edithLogo from "../../assets/edithLogo.png";
import editLogo from "../../assets/edit.png";
import copyLogo from "../../assets/copy.png";
import gfm from "remark-gfm";

interface portfolioProp {
  userGitAccount: string;
}
function RepoPortfolio({ userGitAccount }: portfolioProp) {
  const data = travelCommunityProject;
  const handleSave = () => {};
  const handleCopy = () => {
    navigator.clipboard.writeText(data.contents);
  };
  return (
    <>
      <div className="flex flex-col items-center gap-12">
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
            <div className="py-8 pl-8 pr-8 bg-white/60 rounded-3xl flex-col gap-6 inline-flex">
              <div className="flex gap-2 justify-end">
                <img className="w-8 h-8 cursor-pointer" src={editLogo} />
                <img
                  className="w-8 h-8 cursor-pointer"
                  src={copyLogo}
                  onClick={handleCopy}
                />
              </div>
              <ReactMarkdown remarkPlugins={[gfm]}>
                {data.contents}
              </ReactMarkdown>
            </div>
          </div>
        </div>
        <div
          className="p-1.5 mb-8 bg-black rounded-2xl justify-center items-center inline-flex"
          onClick={handleSave}
        >
          <p className="w-32 text-center text-white text-lg font-medium">
            저장하기
          </p>
        </div>
      </div>
    </>
  );
}

export default RepoPortfolio;
