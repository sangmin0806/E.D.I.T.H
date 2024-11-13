import { formatDate } from "../../types/dataType";
import { useNavigate } from "react-router-dom";

interface ElementProps {
  key: number;
  id: number;
  portfolioName: string;
  repoName: string;
  savedDate: string;
}
function PortfolioListBox({
  key,
  id,
  portfolioName,
  repoName,
  savedDate,
}: ElementProps) {
  const navigate = useNavigate();
  const handleClick = () => {
    //string일수도 있으니까 한번 더 확인하깅
    // navigate(`/repo/detail/${id}`);
    navigate(`/portfolio`);
  };
  return (
    <>
      <div
        className="flex rounded-3xl shadow-custom justify-between w-full p-4 bg-white/30 "
        onClick={handleClick}
      >
        <div className="flex flex-col ">
          <p className="font-semibold text-xl">{portfolioName}</p>
          <p className="font-normal text-base">{repoName}</p>
        </div>
        <div className="flex flex-col items-end h-full justify-center gap-3">
          <p className="font-normal text-base"> {savedDate}</p>
        </div>
      </div>
    </>
  );
}
export default PortfolioListBox;
