import mainLeft from "../../assets/main_left.png";
import mainRight from "../../assets/main_right.png";
import logo from "../../assets/logo.png";

import { useNavigate } from "react-router-dom";
import { motion } from "framer-motion";
import { useRedirectIfLoggedIn } from "../../hooks/useAuth";

function FinishJoinPage() {
  const navigate = useNavigate();
  const handleGoMain = () => {
    navigate("/");
  };
  const handleRegisterFace = () => {
    navigate("/register-face");
  };
  useRedirectIfLoggedIn();
  return (
    <>
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
        transition={{ ease: "easeOut", duration: 2 }}
      >
        <div className="w-[100vw] h-[100vh] flex justify-center items-center">
          <div className="z-20 flex flex-col gap-6 items-center">
            <div className="flex flex-col gap-4">
              <p className="text-center text-black text-2xl font-semibold">
                EDITH 회원가입이
                <br />
                성공적으로 완료되었습니다 !
              </p>
              <p className="text-center text-black text-2xl font-normal">
                얼굴인식 로그인 등록도 진행하시겠습니까?
              </p>
            </div>
            <div className="flex gap-4">
              <div onClick={handleRegisterFace} className="p-2 bg-black rounded-2xl justify-center items-center gap-2.5 inline-flex">
                <p className="min-w-36 text-center text-white text-xl font-medium font-['Pretendard Variable']">
                  얼굴 등록 진행
                </p>
              </div>
              <div className="p-2 bg-white rounded-2xl border border-black justify-center items-center gap-2.5 inline-flex">
                <p
                  className="text-center text-black text-xl font-medium font-['Pretendard Variable']"
                  onClick={handleGoMain}
                >
                  아니요, 다음에 할게요
                </p>
              </div>
            </div>
          </div>
        </div>
      </motion.div>
    </>
  );
}
export default FinishJoinPage;
