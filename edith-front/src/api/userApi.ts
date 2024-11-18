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
    accessToken?: string;
    refreshToken?: string;
    userId: number | null;
    username?: string;
    name?: string;
    email?: string;
    profileImageUrl?: string;
    similarity_score: number | null;
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
    response: {
      userId: result.response?.userId ?? null,
      similarity_score: result.response?.similarity_score ?? null,
      accessToken: result.success ? result.response?.accessToken : undefined,
      refreshToken: result.success ? result.response?.refreshToken : undefined,
      username: result.success ? result.response?.username : undefined,
      name: result.success ? result.response?.name : undefined,
      email: result.success ? result.response?.email : undefined,
      profileImageUrl: result.success ? result.response?.profileImageUrl : undefined,
    },
    error: result.error,
  };
};

