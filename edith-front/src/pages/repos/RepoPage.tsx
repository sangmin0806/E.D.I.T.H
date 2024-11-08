import { useComponentStore } from "../../store/repoPageStore";
import UserHeader from "../../componets/header/UserHeader";
import RepoListContainer from "../../componets/project/RepoListContainer";
import ProjectEnrollContainer from "../../componets/project/ProjectEnrollContainer";
import ProjectModifyContainer from "../../componets/project/ProjectModifyContainer";
import { useEffect, useState } from "react";
import NotFoundErrorPage from "../common/NotFoundErrorPage";
import { useRedirectIfNotLoggedIn } from "../../hooks/useAuth.";
function RepoPage() {
  useRedirectIfNotLoggedIn();
  const showProject = useComponentStore((state) => state.showProject);
  const setShowListNum = useComponentStore(
    (state) => state.setShowComponentOne
  );
  const selectedProjectID = useComponentStore(
    (state) => state.selectedProjectID
  );
  useEffect(() => {
    setShowListNum();
  }, []);
  //나중에 로그인 후 저장된 storage에서 가져올 데이터
  const data = {
    account: "ssafy",
    accountImg: "https://imgur.com/t93p7DF",
  };

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
      <div className="flex w-[100vw] min-h-screen bg-[#F5F6F6] gap-[1rem]">
        <main className=" w-full flex flex-col mt-4 mb-4 ml-[148px] mr-12 gap-[3rem]">
          <UserHeader userGitAccount={data.account} />
          <div>{renderComponent()}</div>
        </main>
      </div>
    </>
  );
}

export default RepoPage;
