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

export const logoutRequest = async (): Promise<{
  success: boolean;
  error?: string;
}> => {
  const result = await apiRequest(() =>
    axiosInstance.post("/api/v1/users/logout")
  );
  return { success: result.success, error: result.error };
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
  embeddingVectors: number[][];
}): Promise<{
  success: boolean;
  error?: string;
}> => {
  const result = await apiRequest(() =>
    axiosInstance.post("/api/v1/users/face/register", {
      embeddingVectors: embeddings.embeddingVectors,
    })
  );
  return {
    success: result.success,
    error: result.error,
  };
};
export const faceLoginRequest = async (
  vectorData: number[]
): Promise<{
  success: boolean;
  response?: {
    accessToken: string;
    refreshToken: string;
    userId: number;
    username: string;
    name: string;
    email: string;
    profileImageUrl: string;
    similarity_score: number;
  };
  error?: string;
}> => {
  const result = await apiRequest(() =>
    axiosInstance.post("/api/v1/face-recognition/face-login", {
      vector: vectorData,
    })
  );

  return {
    success: result.success,
    response: result.success
      ? {
          accessToken: result.response?.accessToken,
          refreshToken: result.response?.refreshToken,
          userId: result.response?.userId,
          username: result.response?.username,
          name: result.response?.name,
          email: result.response?.email,
          profileImageUrl: result.response?.profileImageUrl,
          similarity_score: result.response?.similarity_score,
        }
      : undefined,
    error: result.error,
  };
};
