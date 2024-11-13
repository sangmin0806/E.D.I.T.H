import { useComponentStore } from "../../store/repoPageStore";
import React, { useState } from "react";
import defaultImg from "../../assets/defaultImg.jpg";
import { useNavigate, useParams } from "react-router-dom";

interface UserProps {
  userGitAccount: string;
  showDashboard: boolean;
}

const RepoHeader = React.memo(
  ({ userGitAccount, showDashboard }: UserProps) => {
    const navigate = useNavigate();
    const { projectID } = useParams();
    const handleMoveToPortfolio = () => {
      navigate(`/portfolio/${projectID}`);
    };
    const handleMoveToDashboard = () => {
      navigate(`/dashboard/${projectID}`);
    };

    return (
      <div className="flex justify-center ml-4 mr-4">
        <div className="w-full flex justify-between items-center">
          <div className="flex flex-col">
            <div className="flex gap-2 items-center">
              <p className="text-black text-[24px] font-semibold">
                ProjectName
              </p>
              {showDashboard ? (
                <div
                  className="h-7 px-3 py-1 bg-black rounded-2xl justify-center items-center inline-flex"
                  onClick={handleMoveToPortfolio}
                >
                  <div className="text-white text-base font-medium">
                    나의 포트폴리오 생성
                  </div>
                </div>
              ) : (
                <div
                  className="h-7 px-3 py-1 bg-white border border-black rounded-2xl justify-center items-center inline-flex"
                  onClick={handleMoveToDashboard}
                >
                  <div className="text-black text-base font-medium">
                    대시보드로 돌아가기
                  </div>
                </div>
              )}
            </div>

            <p className="text-black text-[20px] font-light">
              Project Contents
            </p>
          </div>
        </div>
      </div>
    );
  }
);

export default RepoHeader;
