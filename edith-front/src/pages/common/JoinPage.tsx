import mainLeft from "../../assets/main_left.png";
import mainRight from "../../assets/main_right.png";
import logo from "../../assets/logo.png";
import { JoinInfo } from "../../types/userTypes";
import { useState } from "react";
import { registerRequest } from "../../api/userApi";
import { useNavigate } from "react-router-dom";
import { useRedirectIfLoggedIn } from "../../hooks/useAuth";

function JoinPage() {
  // 상태 정의.   
  const [joinInfo, setJoinInfo] = useState<JoinInfo>({
    email: "",
    password: "",
    vcsAccessToken: "",
    vcs: true, // default를 gitLab으로 설정
  });
  const navigate = useNavigate();
  useRedirectIfLoggedIn();

  // 비밀번호 유효성 검사 상태
  const [passwordError, setPasswordError] = useState<string>("");

  const handleToSignup = async () => {
    try {
      // API 명세
      const response = await registerRequest(joinInfo);
      if (!response.success) {
        throw new Error(response.error || "");
      }
      console.log(joinInfo);
      navigate("/join/finish");
    } catch (error) {
      alert(error);
    }
  };
  // 입력 값 변경 핸들러
  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setJoinInfo((prev) => ({
      ...prev,
      [name]: value,
    }));

    // 비밀번호 유효성 검사
    if (name === "pw") {
      if (value.length < 8 || !/[!@#$%^&*(),.?":{}|<>]/.test(value)) {
        setPasswordError(
          "비밀번호는 8자 이상이며 특수 문자가 포함되어야 합니다."
        );
      } else {
        setPasswordError("");
      }
    }
  };

  // 라디오 버튼 변경 핸들러
  const handleRadioChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setJoinInfo((prev) => ({
      ...prev,
      gitLab: e.target.value === "gitlab",
    }));
  };
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
      <div className="flex justify-center items-center h-[100vh]">
        <div className="w-[720px] h-[480px] px-16 py-12 bg-white/60 rounded-3xl border border-black flex-col justify-center items-center gap-12 inline-flex z-20">
          <div className="flex-col items-center gap-4 flex">
            <div className="w-full">
              <img src={logo} className="w-40" />
            </div>

            <div className="h-56 flex-col justify-center items-center gap-4 flex">
              <div className="justify-start items-center gap-4 inline-flex">
                <div className="w-40 text-black text-xl font-semibold">
                  이메일
                </div>
                <input
                  name="email"
                  className="w-96 p-3 bg-white rounded-2xl border border-zinc-400"
                  value={joinInfo.email}
                  onChange={handleInputChange}
                />
              </div>
              <div className="justify-start items-center gap-4 inline-flex">
                <div className="w-40 text-black text-xl font-semibold">
                  비밀번호
                </div>
                <div className="flex flex-col">
                  <input
                    name="password"
                    type="password"
                    className="w-96 p-3 bg-white rounded-2xl border border-zinc-400"
                    value={joinInfo.password}
                    onChange={handleInputChange}
                  />
                  {passwordError && (
                    <p className="text-red-500">{passwordError}</p>
                  )}
                </div>
              </div>
              <div className="justify-start items-center gap-4 inline-flex">
                <div className="w-40 text-black text-xl font-semibold">
                  git Personal Access Token
                </div>
                <input
                  name="vcsAccessToken"
                  className="w-96 p-3 bg-white rounded-2xl border border-zinc-400"
                  value={joinInfo.vcsAccessToken}
                  onChange={handleInputChange}
                />
              </div>
              <div className="justify-start items-center gap-4 inline-flex">
                <div className="flex items-center me-4">
                  <input
                    id="inline-radio"
                    type="radio"
                    value="gitlab"
                    name="inline-radio-group"
                    checked={joinInfo.vcs}
                    onChange={handleRadioChange}
                    className="w-6 h-6 text-blue-600 bg-gray-100 border-gray-300 focus:ring-blue-500 dark:focus:ring-blue-600 dark:ring-offset-gray-800 focus:ring-2 dark:bg-gray-700 dark:border-gray-600"
                  />
                  <label
                    htmlFor="inline-radio"
                    className="ms-2 text-[20px] font-medium text-gray-900 dark:text-gray-300"
                  >
                    git lab
                  </label>
                </div>
                <div className="flex items-center">
                  <input
                    disabled
                    id="inline-disabled-radio"
                    type="radio"
                    value=""
                    name="inline-radio-group"
                    className="w-6 h-6 text-blue-600 bg-gray-100 border-gray-300 focus:ring-blue-500 dark:focus:ring-blue-600 dark:ring-offset-gray-800 focus:ring-2 dark:bg-gray-700 dark:border-gray-600"
                  />
                  <label
                    htmlFor="inline-disabled-radio"
                    className="ms-2 text-[20px] font-medium text-gray-400 dark:text-gray-500"
                  >
                    git hub
                  </label>
                </div>
              </div>
            </div>
          </div>
          <div
            className="p-1.5 bg-black rounded-2xl justify-center items-center gap-2.5 inline-flex cursor-pointer"
            // onClick={() => window.alert('죄송합니다. 자율 프로젝트 발표 기간 이후에 회원 가입이 가능합니다!')}
            onClick={handleToSignup}
          >
            <p className="w-36 text-center text-white text-xl font-medium font-['Pretendard Variable']">
              회원가입
            </p>
          </div>
        </div>
      </div>
    </>
  );
}

export default JoinPage;
