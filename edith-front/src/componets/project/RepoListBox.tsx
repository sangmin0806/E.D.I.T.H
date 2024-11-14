import { formatDate } from "../../types/dataType";
import PlugImg from "../../assets/plus.png";
import profileImg from "../../assets/profile.jpg";
import { useNavigate } from "react-router-dom";
import { useState } from "react";
import KebabMenu from "../common/KebabMenu";
import { Branch, Contributor } from "../../types/projectType";
interface listBoxProps {
  id: number;
  name: string;
  content: string;
  updatedAt: string;
  contributors: Contributor[];
}
function RepoListBox({
  id,
  name,
  content,
  updatedAt,
  contributors,
}: listBoxProps) {
  const navigate = useNavigate();
  const handleClick = () => {
    //string일수도 있으니까 한번 더 확인하깅
    navigate(`/dashboard/${id}`);
  };
  return (
    <>
      <div className="flex rounded-3xl shadow-custom justify-between w-full p-4 bg-white/30 ">
        <div className="flex flex-col " onClick={handleClick}>
          <p className="font-semibold text-xl">{name}</p>
          <p className="font-normal text-base">{content}</p>
          <p className="font-normal text-base">Updated {updatedAt}</p>
        </div>
        <div className="flex flex-col items-end h-full justify-center gap-3">
          <KebabMenu projectID={id} />
          <div className="flex -space-x-2">
            <img src={PlugImg} className=" w-7 h-7" />
            {/* <img src={profileImg} className=" w-7 h-7 rounded-full" />
            <img src={profileImg} className=" w-7 h-7 rounded-full" /> */}
            <img
              className="w-7 h-7 rounded-full"
              src={contributors[1].avatarUrl}
            />
            <img
              className="w-7 h-7 rounded-full"
              src={contributors[0].avatarUrl}
            />
          </div>
        </div>
      </div>
    </>
  );
}
export default RepoListBox;
