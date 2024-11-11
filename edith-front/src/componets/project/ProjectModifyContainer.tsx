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
  const [initialData, setInitialData] = useState<FormValues | undefined>(
    undefined
  );
  const navigate = useNavigate();
  useEffect(() => {
    //api 통신하기 !!!!!
    const data: FormValues = {
      projectId: 12345,
      title: "기존 프로젝트 이름",
      description: "기존 설명",
      branches: ["main", "dev"],
    };
    setInitialData(data);

    // getAPI();
  }, []);

  const handleSave = (data: FormValues) => {
    console.log("수정된 데이터:", data);
    // // 수정 API 호출 !!!!!!!!!!
    // modifyAPI(data);
    toggleComponent(1);
  };

  const modifyAPI = (data: FormValues) => {
    try {
      const request = async () => {
        const result = await projectModifyRequest(data);
        if (!result.success) {
          throw new Error("설정 수정 중 서버 에러가 발생하였습니다.");
        }
      };
      request();
    } catch (error) {
      alert(error);
      navigate(0);
    }
  };

  const getAPI = () => {
    try {
      const request = async () => {
        const result = await projectGetRequest(selectedProjectID);
        if (!result.success || !result.response) {
          throw new Error(result.error);
        }
        setInitialData(result.response);
      };
      request();
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
