import exampleProfileImg from "../../assets/profile.jpg";
import { useComponentStore } from "../../store/repoPageStore";

interface UserProps {
  userGitAccount: string;
  userImgSrc: string;
  showDashboard: boolean;
}

function RepoHeader({ userGitAccount, userImgSrc, showDashboard }: UserProps) {
  const togglePortfolio = useComponentStore((state) => state.togglePortfolio);

  return (
    <>
      <div className="flex justify-center ml-4 mr-4">
        <div className="w-full flex justify-between items-center ">
          <div className="flex flex-col">
            <div className="flex gap-2 items-center">
              <p className="text-black text-[24px] font-semibold">
                ProjectName
              </p>
              {showDashboard && (
                <div
                  className="h-7 px-3 py-1 bg-black rounded-2xl justify-center items-center inline-flex"
                  onClick={togglePortfolio}
                >
                  <div className="text-white text-base font-medium">
                    나의 포트폴리오 생성
                  </div>
                </div>
              )}
            </div>

            <p className="text-black text-[20px] font-light">
              Project Contents
            </p>
          </div>

          <img src={exampleProfileImg} className="w-12 h-12 rounded-full" />
        </div>
      </div>
    </>
  );
}
export default RepoHeader;
