import { useComponentStore } from "../../store/repoPageStore";
import UserHeader from "../../componets/header/UserHeader";
import RepoListContainer from "../../componets/repos/RepoListContainer";
import RepoEnrollContainer from "../../componets/repos/RepoEnrollContainer";
import { useEffect } from "react";
function RepoPage() {
  const showList = useComponentStore((state) => state.showComponentList);
  const setShowListTrue = useComponentStore(
    (state) => state.setShowComponentTrue
  );
  useEffect(() => {
    setShowListTrue();
  }, []);
  //나중에 로그인 후 저장된 storage에서 가져올 데이터
  const data = {
    account: "ssafy",
    accountImg: "https://imgur.com/t93p7DF",
  };

  return (
    <>
      <div className="flex w-[100vw] min-h-screen bg-[#F5F6F6] gap-[1rem]">
        <main className=" w-full flex flex-col mt-4 mb-4 ml-[148px] mr-12 gap-[3rem] justify-center">
          <UserHeader userGitAccount={data.account} />
          {showList ? <RepoListContainer /> : <RepoEnrollContainer />}
        </main>
      </div>
    </>
  );
}

export default RepoPage;
