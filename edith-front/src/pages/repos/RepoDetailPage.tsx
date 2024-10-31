import { useComponentStore } from "../../store/repoPageStore";
import RepoHeader from "../../componets/header/RepoHeader";
import RepoDashboard from "../../componets/repos/RepoDashboard";
import RepoPortfolio from "../../componets/repos/RepoPortfolio";
import Header from "../../componets/header/Header";

function RepoDetailPages() {
  const showDashboard = useComponentStore((state) => state.showDashboard);
  //나중에 로그인 후 저장된 storage에서 가져올 데이터
  const data = {
    account: "ssafy",
    accountImg: "https://imgur.com/t93p7DF",
  };

  return (
    <>
      <div className="flex w-[100vw] bg-[#F5F6F6] gap-[1rem]">
        <Header repoPage={true} />
        <main className="w-full flex flex-col mt-4 mb-4 ml-[148px] mr-12 gap-[3rem]">
          <RepoHeader
            userGitAccount={data.account}
            userImgSrc={data.accountImg}
            showDashboard={showDashboard}
          />
          {showDashboard ? (
            <RepoDashboard />
          ) : (
            <RepoPortfolio userGitAccount={data.account} />
          )}
        </main>
      </div>
    </>
  );
}
export default RepoDetailPages;
