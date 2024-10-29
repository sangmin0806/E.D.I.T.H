import mainLeft from "../../assets/main_left.png";
import mainRight from "../../assets/main_right.png";

function MainPage() {
  return (
    <>
      <img
        src={mainRight}
        className="absolute right-0 top-0 translate-y-8 w-32 z-0" // absolute로 위치 조정
        alt="Right Image"
      />
      <div className="flex justify-center items-center w-full h-screen">
        {/* 중앙 정렬을 위한 flex 설정 */}
        <div className="flex max-w-[1024px] w-full items-center justify-center gap-[-200px]">
          {/* justify-center 추가 */}
          <img src={mainLeft} className="w-[100vh] max-h-[100vh]" />

          <div className="px-12 py-16 bg-white/60 rounded-3xl shadow border border-black flex-col justify-center items-start gap-6 inline-flex z-20">
            <p className="text-black text-3xl font-medium">로그인</p>
            <div className="flex-col justify-center items-center gap-4 inline-flex">
              <div className="flex-col justify-start items-end gap-3 inline-flex">
                <div className="flex-col justify-start items-start gap-4 inline-flex">
                  <input
                    className="w-[320px] text-xl h-12 p-2.5 bg-white border border-zinc-400"
                    placeholder="이메일"
                  />
                  <input
                    className="w-[320px] text-xl h-12 p-2.5 bg-white border border-zinc-400"
                    placeholder="비밀번호"
                  />
                </div>
                <p className="text-right text-neutral-400 text-xl font-light underline">
                  회원가입
                </p>
              </div>
              <div className="flex-col justify-center items-center gap-4 inline-flex">
                <button className="w-[320px] h-10 items-center justify-center bg-black rounded-3xl text-white text-xl font-medium">
                  이메일 로그인
                </button>
                <button className="w-[320px] h-10 items-center justify-center bg-black rounded-3xl text-white text-xl font-medium">
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
