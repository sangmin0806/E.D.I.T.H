import axios, { AxiosResponse } from "axios";
import { JoinInfo, LoginInfo, userInfo } from "../types/userTypes";
import { axiosInstance, apiRequest } from "./axios";

export const loginRequest = async (
  loginInfo: LoginInfo
): Promise<{ success: boolean; response?: userInfo; error?: string }> => {
  return apiRequest(() =>
    axiosInstance.post<userInfo>("/api/v1/users/sign-in", loginInfo)
  );
};

export const registerRequest = async (
  joinInfo: JoinInfo
): Promise<{ success: boolean }> => {
  const result = await apiRequest(() =>
    axiosInstance.post("/api/v1/users/sign-up", joinInfo)
  );
  return { success: result.success };
};
