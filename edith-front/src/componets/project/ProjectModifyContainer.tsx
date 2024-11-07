import { useEffect, useState } from "react";
import { useComponentStore } from "../../store/repoPageStore";
import { FormValues } from "../../types/projectType";
import ProjectFormContainer from "./ProjectFormContainer";
interface ModifyProps {
  selectedProjectID: string;
}
function ProjectModifyContainer({ selectedProjectID }: ModifyProps) {
  const toggleComponent = useComponentStore((state) => state.toggleComponent);
  const [initialData, setInitialData] = useState<FormValues | undefined>(
    undefined
  );
  useEffect(() => {
    //api 통신하기 !!!!!
    const data = {
      projectId: "12345",
      title: "기존 프로젝트 이름",
      description: "기존 설명",
      branches: ["main", "dev"],
    };

    setInitialData(data);
  }, []);

  const handleSave = (data: FormValues) => {
    console.log("수정된 데이터:", data);
    // 수정 API 호출 !!!!!!!!!!
    toggleComponent(1);
  };
  return (
    <>
      <ProjectFormContainer
        mode="modify"
        initialData={initialData}
        onSave={handleSave}
        onCancel={() => toggleComponent(1)}
      />
    </>
  );
}
export default ProjectModifyContainer;
