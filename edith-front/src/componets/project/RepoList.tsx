import { projectList } from "../../assets/dummyData";
import RepoListBox from "./RepoListBox";

function RepoList() {
  // API 통신 후 데이터 (예시 데이터)
  const data = projectList;

  // 각 RepoListBox 클릭 시 /repo/:id 로 이동

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
