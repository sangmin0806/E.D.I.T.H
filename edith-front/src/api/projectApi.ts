import { FormValues, ProjectListItem } from "../types/projectType";
import { apiRequest, axiosInstance } from "./axios";

export const projectEnrollRequest = async (
  formValues: FormValues
): Promise<{ success: boolean }> => {
  const result = await apiRequest(() =>
    axiosInstance.post("/api/v1/projects", formValues)
  );
  return { success: result.success };
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
