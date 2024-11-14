import { motion } from "framer-motion";

import mainLeft from "../../assets/main_left.png";
import mainRight from "../../assets/main_right.png";
import logo from "../../assets/logo.png";
import notfound from "../../assets/not_found.png";
import { useNavigate } from "react-router-dom";

function NotFoundErrorPage() {
  const navigate = useNavigate();
  const handleGoMain = () => {
    navigate("/");
  };

  const handleGoPast = () => {
    navigate(-1);
  };
  return (
    <>
      <div className="h-[100vh]">
        <img
          src={mainRight}
          className="absolute right-0 top-0 translate-y-8 w-32 z-0" // absolute로 위치 조정
          alt="Right Image"
        />
        <img
          src={mainLeft}
          className="absolute left-[150px] w-40vh max-h-[100vh] z-0"
        />
        <img
          src={logo}
          className="relative left-[150px] top-[100px] w-40"
          alt="Logo Image"
        />
        <motion.div
          initial={{ opacity: 0, y: 50 }} // 시작 위치: 아래에 있고 투명함
          animate={{ opacity: 1, y: 0 }} // 애니메이션 끝: 위로 올라오면서 보이게 됨
          transition={{ ease: "easeOut", duration: 1 }}
          className="h-[90%]"
        >
          <div className="w-[100vw] h-full flex justify-center items-center gap-12">
            <img src={notfound} className="w-[320px] z-20" />
            <div className="z-20 flex flex-col gap-6 items-start">
              <div className="flex flex-col gap-4">
                <p className="text-left text-black text-3xl font-normal">
                  페이지를 찾을 수 없습니다.
                </p>
                <p className="text-left text-black text-base font-normal">
                  페이지가 존재하지 않거나, 사용할 수 없는 페이지입니다.
                  <br />
                  입력하신 주소가 정확한지 다시 한 번 확인해주세요.
                </p>
              </div>
              <div className="flex gap-4">
                <div
                  className="p-2 bg-white border border-black justify-center items-center gap-2.5 inline-flex cursor-pointer"
                  onClick={handleGoPast}
                >
                  <p className="text-center text-black text-xl font-medium font-['Pretendard Variable']">
                    이전페이지
                  </p>
                </div>
                <div
                  className="p-2 bg-black justify-center items-center gap-2.5 inline-flex"
                  onClick={handleGoMain}
                >
                  <p className="min-w-12 text-center text-white text-xl font-medium font-['Pretendard Variable'] cursor-pointer">
                    홈
                  </p>
                </div>
              </div>
            </div>
          </div>
        </motion.div>
      </div>
    </>
  );
}
export default NotFoundErrorPage;
