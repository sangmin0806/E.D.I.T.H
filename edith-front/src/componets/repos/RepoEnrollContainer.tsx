import { useState } from "react";
import { useComponentStore } from "../../store/repoPageStore";

function RepoEnrollContainer() {
  const toggleComponent = useComponentStore((state) => state.toggleComponent);
  const [isChecked, setIsChecked] = useState(false);
  const [savedValue, setSavedValue] = useState(null);
  const handleToggle = () => {
    setIsChecked(!isChecked);
  };

  const handleSave = () => {
    //api 통신 ~!~!
    toggleComponent();
  };

  return (
    <>
      <div className="flex items-center justify-center">
        <div className="w-full max-w-[840px] px-12 py-6 bg-white/30 rounded-3xl flex-col justify-center gap-4 inline-flex">
          <h2 className="text-black text-2xl font-bold">New Repository</h2>
          <div className="w-full px-8 py-8 bg-white/30 rounded-3xl flex-col justify-center shadow-custom items-center gap-6 inline-flex">
            <div className="flex w-full items-center justify-center">
              <p className="w-64 text-black text-lg font-medium">
                레포지토리 URL
              </p>
              <input className="flex h-8 w-full max-w-[400px] bg-white rounded-2xl border border-zinc-400" />
            </div>

            <div className="flex w-full items-center justify-center">
              <p className="w-64 text-black text-lg font-medium">
                레포지토리 이름
              </p>
              <input className="flex h-8 w-full max-w-[400px] bg-white rounded-2xl border border-zinc-400" />
            </div>

            <div className="flex w-full items-center justify-center">
              <p className="w-64 text-black text-lg font-medium">
                Personal token
              </p>
              <input className="flex h-8 w-full max-w-[400px] bg-white rounded-2xl border border-zinc-400" />
            </div>

            <div className="flex w-full items-center justify-center">
              <p className="w-64 text-black text-lg font-medium">branch 이름</p>
              <input className="flex h-8 w-full max-w-[400px] bg-white rounded-2xl border border-zinc-400" />
            </div>

            <div className="flex w-full justify-center">
              <p className="w-64 text-black text-lg font-medium">설명 (선택)</p>
              <textarea className="flex h-16 w-full max-w-[400px] bg-white rounded-2xl border border-zinc-400" />
            </div>

            <div className="flex w-full justify-center">
              <p className="w-64 text-black text-lg font-medium">코드 리뷰</p>
              <label className="w-full max-w-[400px] inline-flex items-center cursor-pointer">
                <input
                  type="checkbox"
                  className="sr-only peer"
                  checked={isChecked}
                  onChange={handleToggle} // 체크 상태 토글
                />
                <div className="relative w-11 h-6 bg-gray-200 rounded-full peer peer-focus:ring-4 peer-focus:ring-blue-300 dark:peer-focus:ring-blue-800 dark:bg-gray-700 peer-checked:after:translate-x-full rtl:peer-checked:after:-translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-0.5 after:start-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all dark:border-gray-600 peer-checked:bg-blue-600"></div>
              </label>
            </div>
            <div className="flex gap-2 mt-4">
              <div
                className="p-1 bg-white rounded-2xl border border-black justify-center items-center inline-flex"
                onClick={toggleComponent}
              >
                <p className="w-24 text-center text-black text-lg font-medium">
                  취소하기
                </p>
              </div>
              <div
                className="p-1 bg-black rounded-2xl justify-center items-center inline-flex"
                onClick={handleSave}
              >
                <p className="w-24 text-center text-white text-lg font-medium">
                  등록하기
                </p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </>
  );
}
export default RepoEnrollContainer;
