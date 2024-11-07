import axios, { AxiosResponse } from "axios";

const BASE_URL =
  import.meta.env.VITE_NOW_BASEURL === "local"
    ? import.meta.env.VITE_API_LOCAL_URL
    : import.meta.env.VITE_API_DEPLOYED_URL;

export const axiosInstance = axios.create({
  baseURL: BASE_URL,
  headers: {
    "Content-Type": "application/json",
  },

  timeout: 30000, // 30초 이상 응답 없으면 요청 취소
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
      error.response.headers["Token-Status"] === "expired" &&
      !originalRequest._retry
    ) {
      originalRequest._retry = true;
      try {
        const newToken = await refreshToken();
        return axiosInstance(originalRequest);
      } catch (refreshError) {
        window.location.href = "/login";
        return Promise.reject(refreshError);
      }
    }
    return Promise.reject(error);
  }
);

const refreshToken = async (): Promise<string> => {
  const response = await axiosInstance.post(
    "/users/token/refresh",
    {},
    {
      withCredentials: true,
    }
  );

  return response.data.accessToken;
};

export const apiRequest = async <T>(
  requestFn: () => Promise<AxiosResponse<T>>
): Promise<{ success: boolean; response?: T; error?: string }> => {
  try {
    const response = await requestFn();
    return { success: true, response: response.data };
  } catch (error) {
    let errorMessage = "알 수 없는 오류가 발생했습니다.";
    if (axios.isAxiosError(error)) {
      // AxiosError 타입 확인 및 처리
      errorMessage = error.response?.data?.message || error.message;
    } else if (error instanceof Error) {
      // 일반 JavaScript Error 처리
      errorMessage = error.message;
    }
    console.error(`User API Request: ${errorMessage}`);
    return { success: false, error: errorMessage };
  }
};
