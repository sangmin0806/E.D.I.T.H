import { useNavigate } from "react-router-dom";
import RepoListBox from "./RepoListBox";

function RepoList() {
  const navigate = useNavigate();

  // API 통신 후 데이터 (예시 데이터)
  const data = [
    {
      id: 1,
      subject: "Project Alpha",
      content: "Building a responsive web application.",
      recentDate: new Date("2024-10-15"),
      codeReview: true,
      teamMemberImg: [
        "https://example.com/img/member1.jpg",
        "https://example.com/img/member2.jpg",
        "https://example.com/img/member3.jpg",
      ],
    },
    {
      id: 2,
      subject: "Project Beta",
      content: "Implementing authentication and authorization.",
      recentDate: new Date("2024-10-20"),
      codeReview: false,
      teamMemberImg: [
        "https://example.com/img/member4.jpg",
        "https://example.com/img/member5.jpg",
      ],
    },
    {
      id: 3,
      subject: "Project Gamma",
      content: "Developing real-time data processing pipeline.",
      recentDate: new Date("2024-10-28"),
      codeReview: true,
      teamMemberImg: [
        "https://example.com/img/member6.jpg",
        "https://example.com/img/member7.jpg",
        "https://example.com/img/member8.jpg",
        "https://example.com/img/member9.jpg",
      ],
    },
  ];

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
