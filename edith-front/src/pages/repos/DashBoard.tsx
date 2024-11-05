import RepoHeader from "../../componets/header/RepoHeader";
import RepoDashboard from "../../componets/project/RepoDashboard";

function RepoDetailPages() {
  //나중에 로그인 후 저장된 storage에서 가져올 데이터
  const data = {
    account: "ssafy",
    accountImg: "https://imgur.com/t93p7DF",
  };

  return (
    <>
      <div className="flex min-h-screen bg-[#F5F6F6] gap-[1rem]">
        <main className="w-full flex flex-col mt-4 mb-4 ml-[148px] mr-12 gap-[3rem]">
          <RepoHeader userGitAccount={data.account} showDashboard={true} />
          <RepoDashboard />
        </main>
      </div>
    </>
  );
}
export default RepoDetailPages;
