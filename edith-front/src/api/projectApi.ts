import { BranchData } from "../types/gitGraphType";
import { PortfolioListItem } from "../types/portfolioType";
import {
  FormValues,
  ProjectListItem,
  commitStat,
  myCommitStat,
} from "../types/projectType";
import { apiRequest, axiosInstance } from "./axios";

export const projectEnrollRequest = async (
  formValues: FormValues
): Promise<{ success: boolean }> => {
  const result = await apiRequest(() =>
    axiosInstance.post("/api/v1/projects", formValues)
  );
  return { success: result.success };
};

export const projectGetRequest = async (
  id: number
): Promise<{
  success: boolean;
  response?: FormValues;
  error?: string;
}> => {
  return apiRequest(() => axiosInstance.get(`/api/v1/projects/${id}`));
};

export const projectModifyRequest = async (
  formValues: FormValues
): Promise<{ success: boolean }> => {
  const result = await apiRequest(() =>
    axiosInstance.put("/api/v1/projects", formValues)
  );
  return { success: result.success };
};

export const projectListRequest = async (): Promise<{
  success: boolean;
  response?: ProjectListItem[];
  error?: string;
}> => {
  return apiRequest(() => axiosInstance.get("/api/v1/projects"));
};

export const getGitGraphRequest = async (
  projectId: number
): Promise<{
  success: boolean;
  response?: BranchData[];
  error?: string;
}> => {
  return apiRequest(() =>
    axiosInstance.get(`/api/v1/projects/gitgraph/${projectId}`)
  );
};

export const getCommitStats = async (
  projectId: number
): Promise<{
  success: boolean;
  response?: commitStat;
  error?: string;
}> => {
  return apiRequest(() =>
    axiosInstance.get(`/api/v1/projects/stats?id=${projectId}`)
  );
};

export const getMyCommitsStats = async (): Promise<{
  success: boolean;
  response?: myCommitStat;
  error?: string;
}> => {
  return apiRequest(() => axiosInstance.get(`/api/v1/projects/users/stats`));
};
