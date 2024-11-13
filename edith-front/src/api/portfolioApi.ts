import {
  PortfolioInfo,
  PortfolioListItem,
  PortfolioRequestInfo,
} from "../types/portfolioType";
import { apiRequest, axiosInstance } from "./axios";

export const savePortfolio = async (
  portfolio: PortfolioRequestInfo
): Promise<{ success: boolean; error?: string }> => {
  const result = await apiRequest(() =>
    axiosInstance.post("/api/v1/portfolio/{projectId}", portfolio)
  );
  return {
    success: result.success,
    error: result.error,
  };
};

export const getPortfolioList = async (): Promise<{
  success: boolean;
  response?: PortfolioListItem[];
  error?: string;
}> => {
  return axiosInstance.get("/api/v1/portfolio");
};

export const getPortfolioItem = async (
  id: number
): Promise<{
  success: boolean;
  response?: PortfolioInfo;
  error?: string;
}> => {
  return axiosInstance.get(`/api/v1/portfolio/${id}`);
};

export const makePorfolio = async (
  id: number
): Promise<{ success: boolean; response?: PortfolioInfo; error?: string }> => {
  return axiosInstance.put(`https://edith-ai.xyz:30443/api/v1/portfolio/${id}`);
};
