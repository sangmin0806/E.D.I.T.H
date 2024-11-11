import { useState } from "react";
import { useComponentStore } from "../../store/repoPageStore";
import { FormValues } from "../../types/projectType";
import ProjectFormContainer from "./ProjectFormContainer";
import { projectEnrollRequest } from "../../api/projectApi";

function RepoEnrollContainer() {
  const toggleComponent = useComponentStore((state) => state.toggleComponent);
  const handleSave = (data: FormValues) => {
    console.log(data);
    //api 통신
    // enrollApi(data);
    toggleComponent(1);
  };
  const enrollApi = (data: FormValues) => {
    try {
      const request = async () => {
        const result = await projectEnrollRequest(data);
        if (!result.success) {
          throw new Error("프로젝트 등록 중 서버 에러가 발생하였습니다.");
        }
      };
    } catch (error) {
      alert(error);
    }
  };
  return (
    <>
      <ProjectFormContainer
        mode="enroll"
        onSave={handleSave}
        onCancel={() => toggleComponent(1)}
      />
    </>
  );
}
export default RepoEnrollContainer;
