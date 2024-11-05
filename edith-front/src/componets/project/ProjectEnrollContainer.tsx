import { useState } from "react";
import { useComponentStore } from "../../store/repoPageStore";
import { FormValues } from "../../types/projectType";
import ProjectFormContainer from "./ProjectFormContainer";

function RepoEnrollContainer() {
  const toggleComponent = useComponentStore((state) => state.toggleComponent);
  const handleSave = (data: FormValues) => {
    console.log(data);
    //api 통신
    toggleComponent(1);
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
