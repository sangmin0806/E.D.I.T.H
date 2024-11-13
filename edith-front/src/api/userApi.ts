import { JoinInfo, LoginInfo, userInfo } from "../types/userTypes";
import { axiosInstance, apiRequest } from "./axios";

export const loginRequest = async (
  loginInfo: LoginInfo
): Promise<{
  success: boolean;
  response?: userInfo;
  error?: string;
}> => {
  return apiRequest(() =>
    axiosInstance.post("/api/v1/users/sign-in", loginInfo)
  );
};

export const registerRequest = async (
  joinInfo: JoinInfo
): Promise<{ success: boolean; response?: userInfo; error?: string }> => {
  const result = await apiRequest(() =>
    axiosInstance.post("/api/v1/users/sign-up", joinInfo)
  );

  return {
    success: result.success,
    response: result.response,
    error: result.error, // error를 string으로 변환
  };
};

export const validAccessTokenRequest = async (): Promise<{
  success: boolean;
}> => {
  const result = await apiRequest(() =>
    axiosInstance.post("/api/v1/users/validate")
  );
  return { success: result.success };
};

export const faceRegisterRequest = async (embeddings: {
  embeddingVector: Float32Array[];
}): Promise<{
  success: boolean;
  error?: string;
}> => {
  const result = await apiRequest(() =>
    axiosInstance.post("/api/v1/users/face/register", { embeddingVector: embeddings.embeddingVector })
  );
  return {
    success: result.success,
    error: result.error,
  };
};