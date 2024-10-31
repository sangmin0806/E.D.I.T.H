import { useNavigate } from "react-router-dom";
import { projectList } from "../../assets/dummyData";
import RepoListBox from "./RepoListBox";

function RepoList() {
  const navigate = useNavigate();

  // API 통신 후 데이터 (예시 데이터)
  const data = projectList;

  // 각 RepoListBox 클릭 시 /repo/:id 로 이동
  const handleClick = (id: number) => {
    //string일수도 있으니까 한번 더 확인하깅
    navigate(`/repo/${id}`);
  };

  return (
    <div className="w-full rounded-3xl flex-col justify-center items-center gap-4 inline-flex">
      {data.map((d) => (
        <div key={d.id} onClick={() => handleClick(d.id)} className="w-full">
          <RepoListBox
            subject={d.subject}
            content={d.content}
            recentDate={d.recentDate}
            codeReview={d.codeReview}
            teamMemberImg={d.teamMemberImg}
          />
        </div>
      ))}
    </div>
  );
}

export default RepoList;
