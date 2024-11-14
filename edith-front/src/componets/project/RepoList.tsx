import { useEffect, useState } from "react";
import { projectList } from "../../assets/dummyData";
import RepoListBox from "./RepoListBox";
import { projectListRequest } from "../../api/projectApi";
import { ProjectListItem } from "../../types/projectType";

function RepoList() {
  const [data, setData] = useState<ProjectListItem[]>([]);

  useEffect(() => {
    getListAPI();
  }, []);

  const getListAPI = async () => {
    try {
      const result = await projectListRequest();
      if (!result.success || !result.response) {
        throw new Error(result.error);
      }
      console.log(result);
      if (Array.isArray(result.response)) {
        setData(result.response);
      } else {
        console.error("Expected an array, but got:", result.response);
        setData([]); // 배열이 아니면 빈 배열로 설정
      }
    } catch (error) {
      console.error("API 요청 중 오류 발생:", error);
      alert("데이터를 불러오는 중 오류가 발생했습니다.");
    }
  };

  return (
    <div className="rounded-3xl flex-col justify-center items-center gap-4 inline-flex">
      {data.map((d) => (
        <RepoListBox
          key={d.id}
          id={d.id}
          name={d.name}
          content={d.content}
          updatedAt={d.updatedAt}
          contributors={d.contributors}
        />
      ))}
    </div>
  );
}

export default RepoList;
