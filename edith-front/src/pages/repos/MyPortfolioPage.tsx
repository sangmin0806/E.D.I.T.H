import { useEffect, useState } from "react";
import UserHeader from "../../componets/header/UserHeader";
import PortfolioList from "../../componets/porfolio/PortfolioList";
import { useRedirectIfNotLoggedIn } from "../../hooks/useAuth";
import { userInfo } from "../../types/userTypes";
import { tempUserInfo } from "../../assets/defaultData";

function MyPorfolioPage() {
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
      <div className="flex w-[100vw] min-h-screen bg-[#F5F6F6] justify-center">
        <main className=" w-[80%] flex flex-col gap-[1.5rem] items-center">
          <div className="w-full">
            <UserHeader userGitAccount={userInfo.name} />
          </div>
          <div className="w-full py-6 pl-8 pr-8 bg-white/30 rounded-3xl flex-col justify-center items-center gap-6 inline-flex">
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
