import { useState } from "react";
import { useComponentStore } from "../../store/repoPageStore";
import { FormValues } from "../../types/projectType";
import ProjectFormContainer from "./ProjectFormContainer";
import { projectEnrollRequest } from "../../api/projectApi";

function RepoEnrollContainer() {
  const toggleComponent = useComponentStore((state) => state.toggleComponent);
  const handleSave = (data: FormValues) => {
    //api 통신
    enrollApi(data);
  };
  const enrollApi = async (data: FormValues) => {
    try {
      const result = await projectEnrollRequest(data);
      if (!result.success) {
        throw new Error("프로젝트 등록 중 서버 에러가 발생하였습니다.");
      }

      console.log(data);
    } catch (error) {
      alert(error);
    } finally {
      toggleComponent(1);
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
