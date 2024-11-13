import { useEffect, useState } from "react";
import RepoHeader from "../../componets/header/RepoHeader";
import RepoPortfolio from "../../componets/porfolio/PortfolioContainer";
import { useRedirectIfNotLoggedIn } from "../../hooks/useAuth";
import { userInfo } from "../../types/userTypes";
import { tempUserInfo } from "../../assets/defaultData";

function PortfolioPage() {
  useRedirectIfNotLoggedIn();
  const [userInfo, setUserInfo] = useState<userInfo>(tempUserInfo);
  useEffect(() => {
    const getUserInfo = sessionStorage.getItem("userInfo");
    if (getUserInfo) {
      setUserInfo(JSON.parse(getUserInfo));
    }
  }, []);

  return (
    <>
      <div className="flex min-h-screen bg-[#F5F6F6] gap-[1rem]">
        <main className="w-full flex flex-col mt-4 mb-4 ml-[148px] mr-12 gap-[3rem]">
          <RepoHeader
            userGitAccount={userInfo?.username}
            showDashboard={false}
          />
          userInfo && <RepoPortfolio userGitAccount={userInfo?.username} />
        </main>
      </div>
    </>
  );
}
export default PortfolioPage;
