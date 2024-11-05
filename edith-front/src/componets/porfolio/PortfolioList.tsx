import { dummyPortfolioData } from "../../assets/dummyData";
import PortfolioListBox from "./PortfolioListBox";
function PortfolioList() {
  const data = dummyPortfolioData;
  return (
    <>
      <div className="w-full rounded-3xl flex-col justify-center items-center gap-4 inline-flex">
        {data.map((d) => (
          <PortfolioListBox
            id={d.id}
            portfolioName={d.portfolioName}
            repoName={d.repoProjectName}
            savedDate={d.savedDate}
          />
        ))}
      </div>
    </>
  );
}
export default PortfolioList;
