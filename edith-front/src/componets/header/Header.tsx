import { useNavigate } from "react-router-dom";
import logoImg from "../../assets/header/header-logo.png";
import setting_active from "../../assets/header/setting-active.png";
import defaultImg from "../../assets/defaultImg.jpg";
import { useState } from "react";

function Header({ userImgSrc }: any) {
  const navigate = useNavigate();
  const handleMoveToRepo = () => {
    navigate("/repo");
  };
  const handleToMoveToMyPortfolio = () => {
    navigate("/portfolio/my");
  };
  //나중에 sessiong storage에서 가져오기
  const [imgSrc, setImgSrc] = useState(userImgSrc);
  const handleError = () => {
    setImgSrc(defaultImg); // 이미지 로딩 오류 발생 시 기본 이미지로 변경
  };
  return (
    <>
      <div className="fixed left-0 top-0 w-24 h-full bg-black flex flex-col items-center gap-3">
        <img
          className="border-b-white w-[64px] h-[64px] cursor-pointer"
          src={logoImg}
          onClick={handleMoveToRepo}
        />
        <img
          src={imgSrc}
          className="mt-2 w-12 h-12 rounded-full cursor-pointer"
          onClick={handleToMoveToMyPortfolio}
          onError={handleError}
        />
        <img
          className="w-[64px] h-[64px] cursor-pointer"
          src={setting_active}
        />
      </div>
    </>
  );
}
export default Header;
