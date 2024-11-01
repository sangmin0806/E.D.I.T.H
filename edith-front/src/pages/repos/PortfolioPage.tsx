import RepoHeader from "../../componets/header/RepoHeader";
import RepoPortfolio from "../../componets/repos/RepoPortfolio";

function PortfolioPage() {
  const data = {
    account: "ssafy",
    accountImg: "https://imgur.com/t93p7DF",
  };

  return (
    <>
      <div className="flex min-h-screen bg-[#F5F6F6] gap-[1rem]">
        <main className="w-full flex flex-col mt-4 mb-4 ml-[148px] mr-12 gap-[3rem]">
          <RepoHeader userGitAccount={data.account} showDashboard={true} />
          <RepoPortfolio userGitAccount={data.account} />
        </main>
      </div>
    </>
  );
}
export default PortfolioPage;
