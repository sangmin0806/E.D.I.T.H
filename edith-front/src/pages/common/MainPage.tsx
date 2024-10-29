import { useNavigate } from "react-router-dom";
import { useState } from "react";

import mainLeft from "../../assets/main_left.png";
import mainRight from "../../assets/main_right.png";
import logo from "../../assets/logo.png";
import { LoginInfo } from "../../types/userTypes";

function MainPage() {
  const [login, setLogin] = useState<LoginInfo>({ email: "", pw: "" });
  const navigate = useNavigate();

  // 회원가입 하러 가기
  const handleJoinClick = () => {
    navigate("/join"); // '/join' 경로로 이동
  };

  // 얼굴 인식 로그인 이벤트 발생
  const handleFaceLoginClick = () => {};

  // 매개변수로 email과 pw를 받아 상태를 업데이트한 후 로그인 진행
  const handleEmailLoginClick = () => {};

  return (
    <>
      <img
        src={mainRight}
        className="absolute right-0 top-0 translate-y-8 w-32 z-0" // absolute로 위치 조정
        alt="Right Image"
      />
      <img
        src={mainLeft}
        className="absolute left-[150px] top-150 w-40vh max-h-[100vh] z-0"
      />
      <div className="flex justify-center items-center w-full h-screen">
        {/* 중앙 정렬을 위한 flex 설정 */}
        <div className="flex max-w-[1024px] w-full items-center justify-center gap-20">
          <div className="flex flex-col justify-center items-center gap-2 z-20">
            <p className="text-black text-3xl font-semibold text-center">
              Empowering Developers <br />
              with Intelligent Tools & Highlights
            </p>
            <img src={logo} className="w-[360px]"></img>
          </div>
          <div className="px-12 py-16 bg-white/60 rounded-3xl shadow border border-black flex-col justify-center items-start gap-6 inline-flex z-20">
            <p className="text-black text-3xl font-medium">로그인</p>
            <div className="flex-col justify-center items-center gap-4 inline-flex">
              <div className="flex-col justify-start items-end gap-3 inline-flex">
                <div className="flex-col justify-start items-start gap-4 inline-flex">
                  <input
                    className="w-[320px] text-xl h-12 p-2.5 bg-white border border-zinc-400"
                    placeholder="이메일"
                    onChange={(e) =>
                      setLogin({ ...login, email: e.target.value })
                    }
                  />
                  <input
                    className="w-[320px] text-xl h-12 p-2.5 bg-white border border-zinc-400"
                    placeholder="비밀번호"
                    onChange={(e) => setLogin({ ...login, pw: e.target.value })}
                  />
                </div>
                <p
                  onClick={handleJoinClick}
                  className="text-right text-neutral-400 text-xl font-light underline"
                >
                  회원가입
                </p>
              </div>
              <div className="flex-col justify-center items-center gap-4 inline-flex">
                <button
                  className="w-[320px] h-10 items-center justify-center bg-black rounded-3xl text-white text-xl font-medium"
                  onClick={handleEmailLoginClick}
                >
                  이메일 로그인
                </button>
                <button
                  className="w-[320px] h-10 items-center justify-center bg-black rounded-3xl text-white text-xl font-medium"
                  onClick={handleFaceLoginClick}
                >
                  얼굴인식 로그인
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </>
  );
}

export default MainPage;
