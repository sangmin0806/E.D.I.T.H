import ReactMarkdown from "react-markdown";
import edithLogo from "../../assets/edithLogo.png";
import copyLogo from "../../assets/copy.png";
import { useEffect, useState } from "react";
import LoadingSpinner from "../common/LoadingSpinner";
import {
  getPortfolioItem,
  makePorfolio,
  savePortfolio,
} from "../../api/portfolioApi";
import { useNavigate, useParams } from "react-router-dom";
import { PortfolioInfo } from "../../types/portfolioType";
import Parser from "html-react-parser";

interface portfolioProp {
  userGitAccount: string;
}
function RepoPortfolio({ userGitAccount }: portfolioProp) {
  const { projectID } = useParams();
  const numericProjectID = Number(projectID);
  const [loading, setLoading] = useState(true);
  const [data, setData] = useState<PortfolioInfo | undefined>();
  const [savedPortfolio, setSavedPortfolio] = useState(false);
  const navigate = useNavigate();
  const handleSave = () => {
    console.log("저장 API 시도");
    savePortfolioApi();
  };
  const handleCopy = () => {
    if (!data) return;
    navigator.clipboard.writeText(data.content);
  };
  useEffect(() => {
    if (location.pathname.includes("/portfolio/my")) {
      setSavedPortfolio(true);
      getSavedPortfolioApi();
    } else if (location.pathname.includes("/portfolio")) {
      getPortfolioApi();
    }
  }, []);

  const getSavedPortfolioApi = async () => {
    try {
      const result = await getPortfolioItem(numericProjectID);
      console.log(result);
      if (!result.success) {
        throw new Error(result.error);
      }
      setData(result.response);
    } catch (error) {
      alert(error);
    } finally {
      setLoading(false);
    }
  };
  const getPortfolioApi = async () => {
    try {
      const result = await makePorfolio(numericProjectID);
      console.log(result);
      if (!result.success) {
        throw new Error(result.error);
      }
      setData(result.response);
    } catch (error) {
      alert(error);
    } finally {
      setLoading(false);
    }
  };

  const savePortfolioApi = async () => {
    try {
      if (!data) {
        throw new Error("Data is undefined");
      }
      const result = await savePortfolio(data, numericProjectID);
      if (!result.success) {
        throw new Error(result.error);
      }
      console.log(result);
      navigate("/portfolio/my");
    } catch (error) {
      alert(error);
    }
  };

  const options = {
    replace: (domNode: any) => {
      if (domNode.name === "body") {
        domNode.attribs.style = ""; // body의 스타일 제거
      }
      return domNode;
    },
  };
  if (loading) {
    return (
      <div className="flex w-full h-full items-center justify-center">
        <LoadingSpinner />
      </div>
    );
  }
  return (
    <>
      {data && (
        <div className="flex flex-col items-center gap-12 w-full mb-12">
          <div className="flex flex-col mx-4 gap-8 w-full">
            <div className="flex gap-3">
              <img src={edithLogo} />
              <p className="font-semibold text-lg">
                {userGitAccount} Portfolio
              </p>
            </div>
            <div className="flex flex-col gap-4">
              <div className="px-6 py-10 bg-white/30 rounded-3xl justify-center items-start gap-2.5 inline-flex flex-col">
                <p className="font-semibold text-lg">프로젝트 : {data.name}</p>
                <p className="font-semibold text-lg">
                  기간 : {data.startDate} ~ {data.endDate}
                </p>
              </div>
              <div className="py-8 pl-8 pr-8 bg-white/30 rounded-3xl flex-col gap-6 inline-flex">
                <div className="flex gap-2 justify-end">
                  <img
                    className="w-8 h-8 cursor-pointer"
                    src={copyLogo}
                    onClick={handleCopy}
                  />
                </div>
                <div className="bg-white/30">
                  {Parser(data.portfolio, options)}
                </div>
              </div>
            </div>
          </div>
          {!savedPortfolio && (
            <div
              className="p-1.5 mb-8 bg-black rounded-2xl justify-center items-center inline-flex cursor-pointer"
              onClick={handleSave}
            >
              <p className="w-32 text-center text-white text-lg font-medium">
                저장하기
              </p>
            </div>
          )}
        </div>
      )}
    </>
  );
}

export default RepoPortfolio;
