import { formatDate } from "../../types/dataType";
import PlugImg from "../../assets/plus.png";
import profileImg from "../../assets/profile.jpg";
import { useNavigate } from "react-router-dom";
import { useState } from "react";
interface listBoxProps {
  id: number;
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
          <div className="flex -space-x-2">
            <img src={PlugImg} className=" w-7 h-7" />
            <img src={profileImg} className=" w-7 h-7 rounded-full" />
            <img src={profileImg} className=" w-7 h-7 rounded-full" />
          </div>

          <label className="inline-flex items-center cursor-pointer font-medium flex gap-2">
            코드리뷰
            <input
              type="checkbox"
              className="sr-only peer"
              checked={isChecked}
              onChange={handleToggle} // 체크 상태 토글
            />
            <div className="relative w-11 h-6 bg-gray-200 rounded-full peer peer-focus:ring-4 peer-focus:ring-blue-300 dark:peer-focus:ring-blue-800 dark:bg-gray-700 peer-checked:after:translate-x-full rtl:peer-checked:after:-translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-0.5 after:start-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all dark:border-gray-600 peer-checked:bg-blue-600"></div>
          </label>
        </div>
      </div>
    </>
  );
}
export default RepoListBox;
