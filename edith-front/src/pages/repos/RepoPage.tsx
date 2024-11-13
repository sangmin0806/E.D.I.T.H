import { useComponentStore } from "../../store/repoPageStore";
import UserHeader from "../../componets/header/UserHeader";
import RepoListContainer from "../../componets/project/RepoListContainer";
import ProjectEnrollContainer from "../../componets/project/ProjectEnrollContainer";
import ProjectModifyContainer from "../../componets/project/ProjectModifyContainer";
import { useEffect, useState } from "react";
import NotFoundErrorPage from "../common/NotFoundErrorPage";
import { useRedirectIfNotLoggedIn } from "../../hooks/useAuth";
import { userInfo } from "../../types/userTypes";
import { tempUserInfo } from "../../assets/defaultData";
function RepoPage() {
  useRedirectIfNotLoggedIn();
  const showProject = useComponentStore((state) => state.showProject);
  const [userInfo, setUserInfo] = useState<userInfo>(tempUserInfo);
  const setShowListNum = useComponentStore(
    (state) => state.setShowComponentOne
  );
  const selectedProjectID = useComponentStore(
    (state) => state.selectedProjectID
  );
  useEffect(() => {
    const getUserInfo = sessionStorage.getItem("userInfo");

    if (getUserInfo) {
      setUserInfo(JSON.parse(getUserInfo));
    }

    setShowListNum();
  }, []);
  //나중에 로그인 후 저장된 storage에서 가져올 데이터

  const renderComponent = () => {
    switch (showProject) {
      case 1:
        return <RepoListContainer />;
      case 2:
        return <ProjectEnrollContainer />;
      case 3:
        return selectedProjectID ? (
          <ProjectModifyContainer selectedProjectID={selectedProjectID} />
        ) : (
          <NotFoundErrorPage />
        );
    }
  };

  return (
    <>
      <div className="flex w-full min-h-screen bg-[#F5F6F6] gap-[1rem] justify-center">
        <main className=" w-[80%] flex flex-col mt-4 mb-4 gap-[3rem] items-center">
          <div className="w-full items-start">
            <UserHeader userGitAccount={userInfo.username} />
          </div>

          <div className="w-full">{renderComponent()}</div>
        </main>
      </div>
    </>
  );
}

export default RepoPage;
