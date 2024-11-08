import UserHeader from "../../componets/header/UserHeader";
import PortfolioList from "../../componets/porfolio/PortfolioList";
import { useRedirectIfNotLoggedIn } from "../../hooks/useAuth.";
interface userProps {
  account: "";
}
// function MyPorfolioPage({ account }: userProps) {
function MyPorfolioPage() {
  useRedirectIfNotLoggedIn();
  const account = "ssafy";
  return (
    <>
      <div className="flex w-[100vw] min-h-screen bg-[#F5F6F6]">
        <main className=" w-full flex flex-col mt-4 mb-4 ml-[148px] mr-12 gap-[1.5rem]">
          <UserHeader userGitAccount={account} />

          <div className="py-6 pl-8 pr-8 bg-white/30 rounded-3xl flex-col justify-center items-center gap-6 inline-flex">
            <div className="flex w-full flex-col gap-6">
              <div className="flex gap-2 items-center">
                <p className=" p-2 font-semibold text-2xl">My Portfolio</p>
              </div>
              <PortfolioList />
            </div>
          </div>
        </main>
      </div>
    </>
  );
}
export default MyPorfolioPage;
