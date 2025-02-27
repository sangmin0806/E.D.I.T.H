import axios, { AxiosResponse } from "axios";
export const axiosInstance = axios.create({
  baseURL: "https://edith-ai.xyz:30443",
  // headers: {
  //   "Content-Type": "application/json",
  // },

  timeout: 300000, // 30초 이상 응답 없으면 요청 취소
  withCredentials: true,
});

// 요청 인터셉터
axiosInstance.interceptors.request.use(
  (config) => {
    return config;
  },
  (error) => Promise.reject(error)
);

// 응답 인터셉터
axiosInstance.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config;

    // 토큰 만료 시 상태 코드와 헤더 확인
    if (
      error.response.status === 401 &&
      error.response.data?.error === "JWT Token is missing" &&
      !originalRequest._retry
    ) {
      originalRequest._retry = true;
      try {
        console.log("토큰 재발급 API 요청");
        const newToken = await refreshToken();
        return axiosInstance(originalRequest);
      } catch (refreshError) {
        window.location.href = "/";
        return Promise.reject(refreshError);
      }
    }
    return Promise.reject(error);
  }
);

const refreshToken = async (): Promise<string> => {
  const response = await axiosInstance.post(
    "/api/v1/users/token/refresh",
    {},
    {
      withCredentials: true,
    }
  );

  return response.data.accessToken;
};

export const apiRequest = async <T>(
  requestFn: () => Promise<AxiosResponse<T>>
): Promise<{
  success: boolean;
  response?: T;
  error?: string;
}> => {
  try {
    const response = await requestFn();
    const extractedResponse = (response.data as any)?.response || response.data;

    return { success: true, response: extractedResponse };
  } catch (error) {
    let errorMessage = "알 수 없는 오류가 발생했습니다.";
    let errorStatus: number | undefined = undefined;
    if (axios.isAxiosError(error)) {
      errorMessage = error.response?.data?.error?.message || error.message;
    }
    console.error(`User API Request: ${errorMessage} (Status: ${errorStatus})`);
    return {
      success: false,
      error: errorMessage,
    };
  }
};
