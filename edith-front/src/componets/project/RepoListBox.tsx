import { formatDate } from "../../types/dataType";
import PlugImg from "../../assets/plus.png";
import profileImg from "../../assets/profile.jpg";
import { useNavigate } from "react-router-dom";
import { useState } from "react";
import KebabMenu from "../common/KebabMenu";
interface listBoxProps {
  id: string;
  subject: string;
  content: string;
  recentDate: Date;
  codeReview: boolean;
  teamMemberImg: string[];
}
function RepoListBox({
  id,
  subject,
  content,
  recentDate,
  codeReview,
  teamMemberImg,
}: listBoxProps) {
  const navigate = useNavigate();
  const [isChecked, setIsChecked] = useState(codeReview);
  const dateStr = formatDate(recentDate);
  const handleToggle = () => {
    //codeReview 상태값 변경하는 API
    setIsChecked(!isChecked);
  };
  const handleClick = () => {
    //string일수도 있으니까 한번 더 확인하깅
    // navigate(`/repo/detail/${id}`);
    navigate(`/dashboard`);
  };
  return (
    <>
      <div className="flex rounded-3xl shadow-custom justify-between w-full p-4 bg-white/30 ">
        <div className="flex flex-col " onClick={handleClick}>
          <p className="font-semibold text-xl">{subject}</p>
          <p className="font-normal text-base">{content}</p>
          <p className="font-normal text-base">Updated {dateStr}</p>
        </div>
        <div className="flex flex-col items-end h-full justify-center gap-3">
          <KebabMenu projectID={id} />
          <div className="flex -space-x-2">
            <img src={PlugImg} className=" w-7 h-7" />
            <img src={profileImg} className=" w-7 h-7 rounded-full" />
            <img src={profileImg} className=" w-7 h-7 rounded-full" />
          </div>
        </div>
      </div>
    </>
  );
}
export default RepoListBox;
