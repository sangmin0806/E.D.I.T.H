import { PortfolioInfo, PortfolioListItem } from "../types/portfolioType";
import { apiRequest, axiosInstance } from "./axios";

export const savePortfolio = async (
  portfolio: PortfolioInfo,
  projectId: number
): Promise<{ success: boolean; error?: string }> => {
  const result = await apiRequest(() =>
    axiosInstance.post(`/api/v1/portfolio/${projectId}`, portfolio)
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
  return apiRequest(() => axiosInstance.get("/api/v1/portfolio"));
};

export const getPortfolioItem = async (
  projectId: number
): Promise<{
  success: boolean;
  response?: PortfolioInfo;
  error?: string;
}> => {
  return apiRequest(() => axiosInstance.get(`/api/v1/portfolio/${projectId}`));
};

export const makePorfolio = async (
  projectId: number
): Promise<{ success: boolean; response?: PortfolioInfo; error?: string }> => {
  return apiRequest(() => axiosInstance.put(`/api/v1/portfolio/${projectId}`));
};
