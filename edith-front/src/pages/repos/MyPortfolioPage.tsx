import RepoHeader from "../../componets/header/RepoHeader";
interface userProps {
  account: "";
}
// function MyPorfolioPage({ account }: userProps) {
function MyPorfolioPage() {
  const account = "EDITH";
  return (
    <>
      <div className="flex min-h-screen bg-[#F5F6F6] gap-[1rem]">
        <main className="w-full flex flex-col mt-4 mb-4 ml-[148px] mr-12 gap-[3rem]">
          <RepoHeader userGitAccount={account} showDashboard={true} />
        </main>
      </div>
    </>
  );
}
export default MyPorfolioPage;
