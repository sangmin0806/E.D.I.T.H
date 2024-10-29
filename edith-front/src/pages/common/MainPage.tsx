import mainLeft from "../../assets/main_left.png";
import mainRight from "../../assets/main_right.png";

function MainPage() {
  return (
    <>
      <div className="flex justify-center items-center w-full h-screen">
        {" "}
        {/* 중앙 정렬을 위한 flex 설정 */}
        <div className="flex max-w-[1024px] w-full items-center justify-center">
          {" "}
          {/* justify-center 추가 */}
          <img src={mainLeft} className="w-auto" alt="Left Image" />
          <div className="px-16 py-20 bg-white/60 rounded-3xl shadow border border-black flex-col justify-center items-start gap-12 inline-flex">
            <p className="text-black text-5xl font-medium">로그인</p>
            <div className="flex-col justify-center items-center gap-6 inline-flex">
              <div className="flex-col justify-start items-end gap-3 inline-flex">
                <div className="flex-col justify-start items-start gap-4 inline-flex">
                  <input
                    className="w-[400px] text-3xl h-16 p-2.5 bg-white border border-zinc-400"
                    placeholder="이메일"
                  />
                  <input
                    className="w-[400px] text-3xl h-16 p-2.5 bg-white border border-zinc-400"
                    placeholder="비밀번호"
                  />
                </div>
                <p className="text-right text-neutral-400 text-2xl font-light underline">
                  회원가입
                </p>
              </div>
              <div className="flex-col justify-center items-center gap-4 inline-flex">
                <button className="w-[400px] h-12 p-2.5 bg-black rounded-3xl text-white text-2xl font-medium">
                  이메일 로그인
                </button>
                <button className="w-[400px] h-12 p-2.5 bg-black rounded-3xl text-white text-2xl font-medium">
                  얼굴인식 로그인
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
      <img
        src={mainRight}
        className="fixed right-0 top-8 translate-y-8 w-32"
        alt="Right Image"
      />
    </>
  );
}

export default MainPage;
