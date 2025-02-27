import { useEffect, useState } from "react";
import { useComponentStore } from "../../store/repoPageStore";
import { FormValues } from "../../types/projectType";
import ProjectFormContainer from "./ProjectFormContainer";
import { projectGetRequest, projectModifyRequest } from "../../api/projectApi";
import { useNavigate } from "react-router-dom";
interface ModifyProps {
  selectedProjectID: number;
}
function ProjectModifyContainer({ selectedProjectID }: ModifyProps) {
  const toggleComponent = useComponentStore((state) => state.toggleComponent);
  const data: FormValues = {
    id: 0,
    name: "",
    content: "",
    branches: [],
  };
  const [initialData, setInitialData] = useState<FormValues>(data);
  const navigate = useNavigate();
  useEffect(() => {
    getAPI();
  }, []);

  const handleSave = (data: FormValues) => {
    console.log("수정된 데이터:", data);
    modifyAPI(data);
    toggleComponent(1);
  };

  const modifyAPI = async (data: FormValues) => {
    try {
      const result = await projectModifyRequest(data);
      if (!result.success) {
        throw new Error("설정 수정 중 서버 에러가 발생하였습니다.");
      }
    } catch (error) {
      alert(error);
      navigate(0);
    }
  };

  const getAPI = async () => {
    try {
      const result = await projectGetRequest(selectedProjectID);
      if (!result.success || !result.response) {
        throw new Error(result.error);
      }
      console.log(result.response);
      setInitialData(result.response);
    } catch (error) {
      alert("프로젝트 조회 중 에러가 발생하였습니다.");
      navigate(0);
    }
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
