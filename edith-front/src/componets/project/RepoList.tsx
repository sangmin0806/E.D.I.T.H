import { useEffect, useState } from "react";
import { projectList } from "../../assets/dummyData";
import RepoListBox from "./RepoListBox";
import { projectListRequest } from "../../api/projectApi";
import { ProjectListItem } from "../../types/projectType";

function RepoList() {
  // API 통신 후 데이터 (예시 데이터)
  const [data, setData] = useState<ProjectListItem[]>([]);
  useEffect(() => {
    setData(projectList);
    // getListAPI();
  }, []);
  const getListAPI = () => {
    try {
      const request = async () => {
        const result = await projectListRequest();
        if (!result.success || !result.response) {
          throw new Error(result.error);
        }
        setData(result.response);
      };
      request();
    } catch (error) {
      alert(error);
    }
  };
  return (
    <div className="w-full rounded-3xl flex-col justify-center items-center gap-4 inline-flex">
      {data.map((d) => (
        <RepoListBox
          id={d.id}
          name={d.name}
          contents={d.contents}
          updatedAt={d.updatedAt}
          contributors={d.contributors}
        />
      ))}
    </div>
  );
}

export default RepoList;
