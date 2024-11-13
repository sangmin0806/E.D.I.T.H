import { useEffect, useState } from "react";
import RepoHeader from "../../componets/header/RepoHeader";
import RepoDashboard from "../../componets/project/RepoDashboard";
import { useRedirectIfNotLoggedIn } from "../../hooks/useAuth";
import { userInfo } from "../../types/userTypes";
import { tempUserInfo } from "../../assets/defaultData";

function RepoDetailPages() {
  //나중에 로그인 후 저장된 storage에서 가져올 데이터
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
      <div className="flex min-h-screen bg-[#F5F6F6] gap-[1rem] justify-center">
        <main className="w-[80%] flex flex-col gap-[3rem] items-center">
          <div className="w-full">
            <RepoHeader userGitAccount={userInfo.name} showDashboard={true} />
          </div>
          <div className="w-full">
            <RepoDashboard />
          </div>
        </main>
      </div>
    </>
  );
}
export default RepoDetailPages;
