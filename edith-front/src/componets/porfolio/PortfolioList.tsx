import { useEffect, useState } from "react";
import { dummyPortfolioData } from "../../assets/dummyData";
import PortfolioListBox from "./PortfolioListBox";
import { PortfolioListItem } from "../../types/portfolioType";
import { getPortfolioList } from "../../api/portfolioApi";
function PortfolioList() {
  const [data, setData] = useState<PortfolioListItem[] | undefined>([]);
  useEffect(() => {
    getListAPI();
  }, []);
  const getListAPI = () => {
    try {
      const request = async () => {
        const result = await getPortfolioList();
        setData(result.response);
        console.log(result.response);
        if (!result.success || !result.response) {
          throw new Error(result.error);
        }
      };
      request();
    } catch (error) {
      alert(error);
    }
  };
  return (
    <>
      <div className="w-full rounded-3xl flex-col justify-center items-center gap-4 inline-flex">
        {data &&
          data.map((d) => (
            <PortfolioListBox
              id={d.projectId}
              portfolioName={d.name}
              repoName={d.content}
              savedDate={d.lastModified}
            />
          ))}
      </div>
    </>
  );
}
export default PortfolioList;
