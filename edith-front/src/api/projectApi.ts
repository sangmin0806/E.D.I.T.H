import { FormValues } from "../types/projectType";
import { apiRequest, axiosInstance } from "./axios";

export const projectEnrollRequest = async (
  formValues: FormValues
): Promise<{ success: boolean }> => {
  const result = await apiRequest(() =>
    axiosInstance.post("/api/v1/projects", formValues)
  );
  return { success: result.success };
};
