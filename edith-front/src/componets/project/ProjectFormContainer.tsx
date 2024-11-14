import { useState } from "react";
import { useComponentStore } from "../../store/repoPageStore";
import { FormValues } from "../../types/projectType";
interface FormContainerProps {
  mode: "enroll" | "modify";
  initialData?: FormValues;
  onSave: (data: FormValues) => void;
  onCancel: () => void;
}
function ProjectFormContainer({
  mode,
  initialData = {
    id: 0,
    name: "",
    content: "",
    branches: [],
  },
  onSave,
  onCancel,
}: FormContainerProps) {
  const [formValues, setFormValues] = useState<FormValues>(initialData);
  const [branches, setBranches] = useState<string[]>(initialData.branches);
  const [branch, setBranch] = useState("");

  const handleInputChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>
  ) => {
    const { name, value } = e.target;
    setFormValues((prevValues) => ({
      ...prevValues,
      [name]: value,
    }));
  };

  const handleAddBranch = () => {
    if (branch.trim() !== "") {
      setBranches([...branches, branch]);
      setBranch(""); // 입력창 초기화
    }
  };
  const handleKeyDown = (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key === "Enter") {
      handleAddBranch();
    }
  };
  const handleRemoveBranch = (index: number) => {
    setBranches(branches.filter((_, i) => i !== index));
  };
  const handleSave = () => {
    onSave({ ...formValues, branches });
  };
  const handleCancle = () => {
    onCancel();
  };

  return (
    <>
      <div className="flex items-center justify-center">
        <div className="w-full max-w-[840px] px-14 py-8 bg-white/30 rounded-3xl flex-col justify-center gap-4 inline-flex">
          <h2 className="text-black text-2xl font-bold">
            {mode === "enroll" ? "New Repository" : "Modify Repository"}
          </h2>
          <div className="w-full px-8 py-12 bg-white/30 rounded-3xl flex-col justify-center shadow-custom items-center gap-6 inline-flex">
            <div className="flex w-full items-center justify-center">
              <p className="w-48 text-black text-lg font-medium">
                git 프로젝트 ID
              </p>
              <input
                name="id"
                value={formValues.id}
                className="flex h-9 w-full max-w-[400px] bg-white rounded-2xl border border-zinc-300 p-4"
                onChange={handleInputChange}
                disabled={mode === "modify"}
              />
            </div>

            <div className="flex w-full items-center justify-center">
              <p className="w-48 text-black text-lg font-medium">
                프로젝트 이름
              </p>
              <input
                value={formValues.name}
                name="name"
                className="flex h-9 w-full max-w-[400px] bg-white rounded-2xl border border-zinc-300 p-4"
                onChange={handleInputChange}
              />
            </div>
            <div className="w-full">
              <div className="flex w-full items-center justify-center">
                <p className="w-48 text-black text-lg font-medium">
                  코드리뷰 할<br /> branch 선택
                </p>
                <div className="flex w-full max-w-[400px] gap-2 items-start">
                  <div className="w-full flex flex-col">
                    <input
                      type="text"
                      value={branch}
                      onChange={(e) => setBranch(e.target.value)}
                      onKeyDown={handleKeyDown}
                      className="flex-grow h-9 bg-white rounded-2xl border border-zinc-300 p-4"
                    />
                    <div className="ml-2 flex gap-2 mt-2 flex-wrap">
                      {branches.map((b, index) => (
                        <div
                          key={index}
                          className="flex justify-start text-gray-700 gap-1"
                        >
                          <p>{b}</p>
                          <p
                            onClick={() => handleRemoveBranch(index)}
                            className="text-gray-700"
                          >
                            x
                          </p>
                        </div>
                      ))}
                    </div>
                  </div>
                  <button
                    onClick={handleAddBranch}
                    className="w-20 h-8 bg-black text-white rounded-lg hover:cursor-pointer"
                  >
                    추가
                  </button>
                </div>
              </div>
            </div>

            <div className="flex w-full justify-center">
              <p className="w-48 text-black text-lg font-medium">설명 (선택)</p>
              <textarea
                className="flex h-20 w-full max-w-[400px] bg-white rounded-2xl border border-zinc-300 p-4"
                name="contents"
                value={formValues.content}
                onChange={handleInputChange}
              />
            </div>

            <div className="flex w-full justify-center"></div>
            <div className="flex gap-2 mt-2">
              <div
                className="p-1 bg-white rounded-2xl border border-black justify-center items-center inline-flex"
                onClick={handleCancle}
              >
                <p className="w-24 text-center text-black text-base font-medium">
                  취소하기
                </p>
              </div>
              <div
                className="p-1 bg-black rounded-2xl justify-center items-center inline-flex"
                onClick={handleSave}
              >
                <p className="w-24 text-center text-white text-base font-medium">
                  {mode === "enroll" ? "등록하기" : "저장하기"}
                </p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </>
  );
}
export default ProjectFormContainer;
